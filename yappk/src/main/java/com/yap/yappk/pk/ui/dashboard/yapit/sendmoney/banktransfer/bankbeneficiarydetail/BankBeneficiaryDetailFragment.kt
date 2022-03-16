package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.permissionx.PermissionX
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankBeneficiaryDetailBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.customers.responsedtos.BankBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.bottomsheetadapter.PrimaryCardDetailMoreViewAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer.BankTransferFragment
import com.yap.yappk.pk.utils.*
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.*
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class BankBeneficiaryDetailFragment :
    BaseNavFragment<PkFragmentBankBeneficiaryDetailBinding, IBankBeneficiaryDetail.State, IBankBeneficiaryDetail.ViewModel>(
        R.layout.pk_fragment_bank_beneficiary_detail
    ),
    IBankBeneficiaryDetail.View, ToolBarView.OnToolBarViewClickListener,
    BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IBankBeneficiaryDetail.ViewModel by viewModels<BankBeneficiaryDetailVM>()
    lateinit var adapter: PrimaryCardDetailMoreViewAdapter
    var chooseBottomDialog: BottomSheetUIDialog? = null
    val chooseLibrary: Int = 0
    val takePhoto: Int = 1
    var selectedAction: Int? = null
    private lateinit var easyImage: EasyImage

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        chooseBottomDialog = getBottomSheetBuilder()
        getFragmentArguments()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnDelete -> showAlertDelete()
            R.id.ivAdd -> routeToBottomDialog()
            R.id.btnSend -> openTransferScreen()
        }
    }

    private fun openTransferScreen() {
        navigate(
            R.id.action_bankBeneficiaryDetailFragment_to_bankTransferFragment,
            bundleOf(BankTransferFragment::class.java.name to viewModel.beneficiary.value, KEY to 0)
        )
    }

    private fun routeToBottomDialog() {
        checkPermission(listOf(Manifest.permission.CAMERA))
        { chooseBottomDialog?.show(childFragmentManager, "Choose") }
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

    private fun showAlertDelete() {
        confirm(
            message = requireContext().getString(
                screen_main_bank_transfer_display_text_pop_up_description
            ),
            positiveButton = requireContext().getString(common_button_delete),
            negativeButton = requireContext().getString(common_button_cancel),
            cancelable = false
        ) {
            viewModel.removeBeneficiary(viewModel.beneficiary.value?.beneficiaryId ?: "0")
        }
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun onEndTextClicked() {
        viewModel.updateBeneficiary(
            viewModel.beneficiary.value?.beneficiaryId ?: "0",
            mViewBinding.etNickName.text.toString().trim()
        )
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setBeneficiary(it.getParcelable(IBeneficiary::class.java.name))
        }
    }

    private fun handleBeneficiary(iBeneficiary: IBeneficiary) {
        mViewBinding.ivImage.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(iBeneficiary.fullName ?: "")
                .setIndex(0)
                .setImageUrl(iBeneficiary.imgUrl)
                .build()
        )
        mViewBinding.tvName.text = iBeneficiary.fullName
        mViewBinding.tvBankName.text = iBeneficiary.bankTitle
        mViewBinding.etNickName.setText(iBeneficiary.subtitle)
        mViewBinding.tvAccount.text =
            if (iBeneficiary.ibanNumber.isNullOrEmpty()) requireContext().getString(
                screen_bank_transfer_beneficiary_detail_display_account_number,
                iBeneficiary.accountNumber ?: ""
            ) else requireContext().getString(
                screen_bank_transfer_beneficiary_detail_display_iban_number,
                iBeneficiary.ibanNumber ?: ""
            )
        mViewBinding.ivBankIcon.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(iBeneficiary.bankTitle ?: "")
                .setImageUrl(iBeneficiary.bankImgUrl)
                .build()
        )
    }

    private fun handleDeleteBeneficiaries(isDeleted: Boolean) {
        if (isDeleted) navigateBackWithResult(resultCode = Activity.RESULT_OK)
    }

    private fun handleUpdateBeneficiaries(isUpdate: Boolean) {
        if (isUpdate) navigateBackWithResult(resultCode = Activity.RESULT_OK)
    }

    private val chooseItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            chooseBottomDialog?.dismiss()
            selectedAction = pos
            openChosenAction(pos)
        }
    }

    private fun openChosenAction(action: Int) {
        when (action) {
            chooseLibrary -> openGalley()
            takePhoto -> dispatchTakePictureIntent() //openCamera()
        }
    }

    private fun openGalley() {
        if (!::easyImage.isInitialized)
            initializeEasyImage()
        easyImage.openGallery(this)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            requireActivity().createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        photoFile?.also { file ->
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                pkBuildConfigurations.configManager?.applicationId + ".provider",
                file
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activityLauncher.launch(takePictureIntent) { result ->
                if (result.resultCode == RESULT_OK) {
                    uploadProfileImage(file)
                }
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

    private fun getBottomSheetBuilder(): BottomSheetUIDialog? {
        adapter =
            PrimaryCardDetailMoreViewAdapter(
                viewModel.getChooseOptionList(),
                chooseItemClickListener
            )
        val builder =
            BottomSheetBuilder().setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        return builder.build()
    }

    private fun openUpdateImageScreen(beneficiary: IBeneficiary) {
        navigateForResult(
            requestCode = BANK_BENEFICIARY_UPDATE_CODE,
            resId = R.id.action_bankBeneficiaryDetailFragment_to_updateBankBeneficiaryImageFragment,
            args = bundleOf(IBeneficiary::class.java.name to beneficiary)
        )
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
        uploadProfileImage(mediaFile.file)
    }

    private fun uploadProfileImage(file: File) {
        val beneficiary = viewModel.beneficiary.value
        if (beneficiary is BankBeneficiary) {
            beneficiary.imageUri = file
            openUpdateImageScreen(beneficiary)
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.resultCode == Activity.RESULT_OK && result.requestCode == BANK_BENEFICIARY_UPDATE_CODE) {
            result.data?.get(IBeneficiary::class.java.name)?.let {
                if (it is IBeneficiary)
                    viewModel.setBeneficiary(it)
            } ?: selectedAction?.let { action ->
                openChosenAction(action)
            }
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.beneficiary, ::handleBeneficiary)
        observe(viewModel.isBeneficiaryDeleted, ::handleDeleteBeneficiaries)
        observe(viewModel.isBeneficiaryUpdated, ::handleUpdateBeneficiaries)

    }

}