package com.yap.yappk.pk.ui.kyc.selfie.takeselfie.camera

import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface ICameraProvider {
    fun startCamera(
        cameraLens: CameraSelector,
        cView: PreviewView,
        lifecycleOwner: LifecycleOwner
    )

    fun takePhoto()
    fun stopCamera()
    fun setCameraListener(listener: CameraProvider.CameraListener)
}