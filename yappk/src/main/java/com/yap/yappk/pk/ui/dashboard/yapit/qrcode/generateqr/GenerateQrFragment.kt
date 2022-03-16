package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.generateqr

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.identityscanner.utils.deleteTempFolder
import com.yap.permissionx.PermissionX
import com.yap.uikit.utils.extensions.generateQRCode
import com.yap.uikit.widget.nameinitialscircleimageview.ColorGenerator
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentGenerateQrBinding
import com.yap.yappk.localization.*
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.utils.generateQrCode
import com.yap.yappk.pk.utils.shareImage
import com.yap.yappk.pk.utils.storeBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenerateQrFragment :
    BaseNavFragment<PkFragmentGenerateQrBinding, IGenerateQr.State, IGenerateQr.ViewModel>(
        R.layout.pk_fragment_generate_qr
    ),
    IGenerateQr.View, ColorGenerator {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IGenerateQr.ViewModel by viewModels<GenerateQrVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvSaveToGallery -> saveQrCode()
            R.id.tvShareMyCode -> shareQrCode()
            R.id.ivBack -> requireActivity().onBackPressed()
            R.id.ivScan -> openScanQrScreen()
        }
    }

    private fun openScanQrScreen() {
        navigateWithPopup(
            R.id.action_generateQrFragment_to_scanQrFragment,
            R.id.generateQrFragment
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewValues()
    }

    private fun setViewValues() {
        mViewBinding.ivQRCode.setImageDrawable(
            requireContext().generateQrCode(
                viewModel.sessionManager.userAccount.value?.encryptedAccountUUID?.generateQRCode()
                    ?: ""
            )
        )
        mViewBinding.ivProfilePic.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(
                    viewModel.sessionManager.userAccount.value?.currentCustomer?.getFullName() ?: ""
                )
                .setColorGenerator(this)
                .setImageUrl(viewModel.sessionManager.userAccount.value?.currentCustomer?.getPicture())
                .build()
        )
        mViewBinding.tvUserName.text =
            viewModel.sessionManager.userAccount.value?.currentCustomer?.getFullName()
    }

    private fun saveQrCode() {
        activity?.storeBitmap(mViewBinding.qrContainer) {
            showToast(requireContext().getString(common_saved_image_to_gallery))
        }
    }

    private fun shareQrCode() {
        requireContext().shareImage(
            mViewBinding.qrContainer,
            imageName = requireContext().getString(
                screen_fragment_qr_code_text_qr_code_image_name
            ),
            shareText = requireContext().getString(
                screen_fragment_qr_code_text_qr_code_share_description,
                viewModel.sessionManager.userAccount.value?.currentCustomer?.getFullName() ?: ""
            ),
            chooserTitle = requireContext().getString(
                screen_fragment_qr_code_text_qr_code_share_description
            ),
            applicationId = pkBuildConfigurations.configManager?.applicationId ?: ""
        )
    }

    private fun checkPermission(permissionsList: List<String>, function: () -> Unit) {
        PermissionX.init(this).permissions(
            permissionsList
        ).onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                requireContext().getString(common_text_permissions_denied),
                requireContext().getString(open_setting)
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                function.invoke()
            } else {
                showToast(requireContext().getString(common_text_permissions_denied))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().deleteTempFolder()
    }

    @SuppressLint("ResourceAsColor")
    override fun generateBackgroundColor(index: Int): Int = R.color.pkBlueWithAHintOfPurpleTwo


    @SuppressLint("ResourceAsColor")
    override fun generateTextColor(index: Int): Int = R.color.pkWhite

}