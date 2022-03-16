package com.yap.yappk.pk.ui.kyc.selfie.takeselfie

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.permissionx.PermissionX
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentTakeSelfieBinding
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.ui.kyc.selfie.takeselfie.camera.CameraProvider
import com.yap.yappk.pk.ui.kyc.selfie.takeselfie.camera.ICameraProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class TakeSelfieFragment :
    BaseNavFragment<FragmentTakeSelfieBinding, ITakeSelfie.State, ITakeSelfie.ViewModel>(R.layout.fragment_take_selfie),
    ITakeSelfie.View, CameraProvider.CameraListener, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ITakeSelfie.ViewModel by viewModels<TakeSelfieVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()
    private var cameraProvider: ICameraProvider? = null
    private var selfiePath: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initializeCamera()
        checkPermission(
            listOf(Manifest.permission.CAMERA)
        ) { startCamera() }
    }

    private fun initializeCamera() {
        cameraProvider = CameraProvider(requireActivity())
        cameraProvider?.setCameraListener(this)
    }

    private fun startCamera() {
        cameraProvider?.startCamera(
            CameraSelector.DEFAULT_FRONT_CAMERA,
            mViewBinding.pvCamera,
            requireActivity()
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnTakePhoto -> {
                cameraProvider?.takePhoto()
            }
        }
    }

    override fun onImageTaken(file: File) {
        selfiePath = file.absolutePath
        navigate(
            R.id.action_takeSelfieFragment_to_selfieReviewFragment,
            bundleOf(TakeSelfieFragment::class.java.name to selfiePath)
        )
    }

    private fun checkPermission(permissionsList: List<String>, function: () -> Unit) {
        PermissionX.init(this).permissions(
            permissionsList
        ).onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                getString(R.string.message_camera_permission_denied),
                getString(R.string.open_setting)
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                function.invoke()
            } else {
                showToast(getString(R.string.common_text_permissions_denied))
            }
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        deleteImage()
    }

    private fun deleteImage() {
        GlobalScope.launch {
            File(
                selfiePath ?: ""
            ).deleteRecursively()
        }
    }

    override fun onFaceDetection(hasFace: Boolean) {
        viewModel.viewState.hasFace.value = hasFace
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider?.stopCamera()
    }

}