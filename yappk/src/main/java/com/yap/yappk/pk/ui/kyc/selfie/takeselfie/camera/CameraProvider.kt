package com.yap.yappk.pk.ui.kyc.selfie.takeselfie.camera

import android.annotation.SuppressLint
import android.app.Activity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.yap.yappk.BuildConfig
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraProvider(
    private val activity: Activity
) : ICameraProvider {
    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File? = null
    private var cameraExecutor: ExecutorService? = null
    private var listener: CameraListener? = null
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private val TAG = "CameraXYap"

    init {
        initializeCamera()
    }

    override fun setCameraListener(listener: CameraListener) {
        this.listener = listener
    }

    private fun initializeCamera() {
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(
                it,
                BuildConfig.LIBRARY_PACKAGE_NAME.replaceBeforeLast("-", "").uppercase()
            ).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity.filesDir
    }

    @SuppressLint("TimberArgCount")
    override fun startCamera(
        cameraLens: CameraSelector,
        cView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = getPreviewBuilder(cView)
            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = getImageAnalyzer()
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraLens, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Timber.e(exc, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    @SuppressLint("TimberArgCount")
    private fun getImageAnalyzer(): UseCase {
        return ImageAnalysis.Builder()
            .build()
            .also {
                cameraExecutor?.let { cameraExecutor ->
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma, hasFace ->
                        Timber.d(TAG, "Average luminosity: $luma")
                        listener?.onFaceDetection(hasFace)
                    })
                }
            }
    }

    private fun getPreviewBuilder(cView: PreviewView): UseCase {
        return Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(cView.surfaceProvider)
            }
    }

    override fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time-stamped output file to hold the image
        val photoFile = createImageFile()
        // Create output options object which contains file + metadata
        val outputOptions = createImageOutputOption(photoFile)
        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            getImageCallBack(photoFile)
        )
    }

    private fun getImageCallBack(photoFile: File): ImageCapture.OnImageSavedCallback {
        return object : ImageCapture.OnImageSavedCallback {
            @SuppressLint("TimberArgCount")
            override fun onError(exc: ImageCaptureException) {
                Timber.e(exc, TAG, "Photo capture failed: ${exc.message}")
            }
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                listener?.onImageTaken(photoFile)
            }
        }
    }

    private fun createImageOutputOption(photoFile: File): ImageCapture.OutputFileOptions {
        return ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(ImageCapture.Metadata().also {
                it.isReversedHorizontal = CameraSelector.LENS_FACING_FRONT == 0
            }).build()
    }

    private fun createImageFile(): File {
        return File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
    }

    override fun stopCamera() {
        cameraExecutor?.shutdown()
    }

    interface CameraListener {
        fun onImageTaken(file: File)
        fun onFaceDetection(hasFace: Boolean) = Unit
    }
}