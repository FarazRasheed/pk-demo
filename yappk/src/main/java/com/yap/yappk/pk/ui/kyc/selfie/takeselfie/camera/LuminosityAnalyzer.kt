package com.yap.yappk.pk.ui.kyc.selfie.takeselfie.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.nio.ByteBuffer

class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        //Handle all the ML logic here
        val mediaImage = imageProxy.image
        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = getFaceDetectionOptions()
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val detector = FaceDetection.getClient(highAccuracyOpts)
            val result = detector.process(image).addOnCompleteListener {
                listener(luma, checkFace(it.result))
                imageProxy.close()
            }
        }
    }

    private fun getFaceDetectionOptions(): FaceDetectorOptions {
        return FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setMinFaceSize(1f)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    }

    private fun checkFace(faces: List<Face>): Boolean {
        for (face in faces) {
            return face.allContours.all { !it.points.isNullOrEmpty() }
        }
        return false
    }
}

typealias LumaListener = (luma: Double, hasFace: Boolean) -> Unit