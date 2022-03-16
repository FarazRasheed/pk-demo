package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.scanqr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.yap.permissionx.PermissionX
import com.yap.uikit.utils.extensions.getQRCode
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentScanQrBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main.YapToYapTransferFragment
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import com.yap.yappk.pk.utils.JPEG
import com.yap.yappk.pk.utils.JPG
import com.yap.yappk.pk.utils.PNG
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.*
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream

@AndroidEntryPoint
class ScanQrFragment :
    BaseNavFragment<PkFragmentScanQrBinding, IScanQr.State, IScanQr.ViewModel>(
        R.layout.pk_fragment_scan_qr
    ),
    IScanQr.View, QRCodeReaderView.OnQRCodeReadListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IScanQr.ViewModel by viewModels<ScanQrVM>()

    private lateinit var easyImage: EasyImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission(listOf(Manifest.permission.CAMERA)) {
            initQRCodeReaderView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivBack -> {
                requireActivity().onBackPressed()
            }
            R.id.ivLibrary -> {
                mViewBinding.qrCodeReaderView.setQRDecodingEnabled(false)
                checkPermission(listOf(Manifest.permission.CAMERA)) {
                    openGalley()
                }
            }
            R.id.ivMyQrCode -> openQrScreen()
        }
    }

    private fun openQrScreen() {
        navigateWithPopup(
            R.id.action_scanQrFragment_to_generateQrFragment,
            R.id.scanQrFragment
        )
    }

    private fun initQRCodeReaderView() {
        mViewBinding.ivOverLay.loadImage(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.pk_bg_qr_scan
            )
        )
        mViewBinding.qrCodeReaderView.setAutofocusInterval(2000L)
        mViewBinding.qrCodeReaderView.setOnQRCodeReadListener(this)
        mViewBinding.qrCodeReaderView.setBackCamera()
        mViewBinding.qrCodeReaderView.startCamera()
        mViewBinding.qrCodeReaderView.setQRDecodingEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            handleImagePickerResult(requestCode, resultCode, data)
        }
    }

    private fun handleImagePickerResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (::easyImage.isInitialized)
            easyImage.handleActivityResult(
                requestCode,
                resultCode,
                data,
                requireActivity(),
                object : DefaultCallback() {
                    override fun onMediaFilesPicked(
                        imageFiles: Array<MediaFile>,
                        source: MediaSource
                    ) {
                        onPhotosReturned(imageFiles, source)
                    }

                    override fun onImagePickerError(
                        @NonNull error: Throwable,
                        @NonNull source: MediaSource
                    ) {
                        //Some error handling
                        showToast(
                            requireContext().getString(
                                screen_bank_transfer_beneficiary_detail_error
                            )
                        )
                    }

                    override fun onCanceled(@NonNull source: MediaSource) {
                        //Not necessary to remove any files manually anymore
                    }
                })
    }

    private fun onPhotosReturned(path: Array<MediaFile>, source: MediaSource) {
        path.firstOrNull()?.let { mediaFile ->
            val ext = mediaFile.file.extension
            if (ext.isNotBlank()) {
                when (ext) {
                    PNG, JPG, JPEG -> onImageReturn(mediaFile)
                    else -> showToast(
                        requireContext().getString(
                            screen_bank_transfer_beneficiary_detail_error
                        )
                    )
                }
            } else {
                showToast(requireContext().getString(screen_bank_transfer_beneficiary_detail_error))
            }
        }
    }

    private fun onImageReturn(mediaFile: MediaFile) {
        val inputStream: InputStream =
            BufferedInputStream(FileInputStream(mediaFile.file))
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        scanQRImage(bitmap)?.let {
            sendQrRequest(it.getQRCode())
        }
    }

    private fun openGalley() {
        if (!::easyImage.isInitialized)
            initializeEasyImage()
        easyImage.openGallery(this)
    }

    override fun onPause() {
        super.onPause()
        mViewBinding.qrCodeReaderView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.qrCodeReaderView.stopCamera()
    }

    private fun checkPermission(permissionsList: List<String>, function: () -> Unit) {
        PermissionX.init(this).permissions(
            permissionsList
        ).onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                requireContext().getString(message_camera_permission_denied),
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

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        mViewBinding.qrCodeReaderView.setQRDecodingEnabled(false)
        sendQrRequest(text?.getQRCode())
    }

    private fun sendQrRequest(qrCode: String?) {
        viewModel.sessionManager.userAccount.value?.encryptedAccountUUID.let { accountId ->
            if (qrCode == accountId) {
                showToast(
                    requireContext().getString(
                        screen_fragment_qr_code_text_qr_code_own_error_msg
                    )
                )
                mViewBinding.qrCodeReaderView.setQRDecodingEnabled(true)
            } else {
                viewModel.getQrCodeUserInfo(qrCode ?: "")
            }
        }
    }

    private fun initializeEasyImage() {
        easyImage =
            EasyImage.Builder(requireContext()) // Chooser only
                .setChooserTitle(
                    requireContext().getString(
                        screen_bank_transfer_beneficiary_detail_chooser_title
                    )
                )
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName(
                    requireContext().getString(
                        screen_bank_transfer_beneficiary_detail_folder_name
                    )
                )
                .allowMultiple(false)
                .build()
    }

    private fun scanQRImage(bMap: Bitmap): String? {
        var contents: String? = null
        val intArray = IntArray(bMap.width * bMap.height)
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource =
            RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bitmap)
            contents = result.text
        } catch (e: Exception) {
            showToast(
                requireContext().getString(
                    screen_fragment_qr_code_text_qr_code_decoding_error_msg
                )
            )
            mViewBinding.qrCodeReaderView.setQRDecodingEnabled(true)
        }
        return contents
    }

    private fun handleUserInfo(iBeneficiary: IBeneficiary?) {
        iBeneficiary?.let {
            openY2YTransferScreen(iBeneficiary)
        } ?: mViewBinding.qrCodeReaderView.setQRDecodingEnabled(true)
    }

    private fun openY2YTransferScreen(contact: IBeneficiary) {
        navigate(
            R.id.action_scanQrFragment_to_pk_y2y_transfer_nav,
            bundleOf(YapToYapTransferFragment::class.java.name to contact)
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.userInfo, ::handleUserInfo)
    }

}
