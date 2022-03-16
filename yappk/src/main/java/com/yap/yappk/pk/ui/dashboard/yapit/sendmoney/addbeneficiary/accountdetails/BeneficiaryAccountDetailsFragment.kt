package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.finish
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.toast
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBeneficiaryAccountDetailsBinding
import com.yap.yappk.databinding.PkLayoutAddBeneficiarySuccessDialogViewBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_beneficiary_account_details_display_text_account_no
import com.yap.yappk.localization.screen_beneficiary_account_details_display_text_iban
import com.yap.yappk.localization.screen_beneficiary_account_details_display_text_iban_or_account_no
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.SendMoneyBeneficiaryType
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails.success.AddBeneficiarySuccessDialogVH
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.AddBeneficiaryVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.IAddBeneficiary
import com.yap.yappk.pk.ui.generic.enums.OTPAction
import com.yap.yappk.pk.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeneficiaryAccountDetailsFragment :
    BaseNavFragment<PkFragmentBeneficiaryAccountDetailsBinding, IBeneficiaryAccountDetails.State, IBeneficiaryAccountDetails.ViewModel>(
        R.layout.pk_fragment_beneficiary_account_details
    ),
    IBeneficiaryAccountDetails.View, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IBeneficiaryAccountDetails.ViewModel by viewModels<BeneficiaryAccountDetailsVM>()
    private val parentViewModel: IAddBeneficiary.ViewModel by activityViewModels<AddBeneficiaryVM>()

    private var mAddBeneficiarySuccessDialogVH: BaseViewHolder? = null
    var mAddBeneficiarySuccessAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        parentViewModel.stateList.value = viewModel.handleState()
        parentViewModel.toolBarVisibility.value = View.VISIBLE
    }

    override fun getFragmentArguments() {
        viewModel.setBankAccountDetails(arguments?.getParcelable(BeneficiaryAccountDetailsFragment::class.java.name))
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnAddBeneficiary -> viewModel.createOtp(OTPAction.IBFT_BENEFICIARY.name)
        }
    }

    private fun setScreenData(bankAccountInfo: BankData?) {
        mViewBinding.ivBankLogo.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankAccountInfo?.bankName ?: "")
                .setIndex(0)
                .setImageUrl(bankAccountInfo?.bankLogoUrl)
                .build()
        )
        mViewBinding.ivBeneficiaryIcon.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankAccountInfo?.accountHolderTitle ?: "")
                .setIndex(0)
                .setImageUrl(null)
                .build()
        )
        mViewBinding.tvBankName.text = bankAccountInfo?.bankName
        mViewBinding.tvBenName.text = bankAccountInfo?.accountHolderTitle
        val accountTitle = if (bankAccountInfo?.accountNumber.isNullOrBlank()) requireContext().getString(
            screen_beneficiary_account_details_display_text_iban_or_account_no,
            requireContext().getString(screen_beneficiary_account_details_display_text_iban),
            bankAccountInfo?.ibanNumber ?: ""
        ) else requireContext().getString(
            screen_beneficiary_account_details_display_text_iban_or_account_no,
            requireContext().getString(screen_beneficiary_account_details_display_text_account_no),
            bankAccountInfo?.accountNumber ?: ""
        )
        mViewBinding.tvAccountNumber.text = accountTitle
    }

    private fun openAddBeneficiarySuccessDialog() {
        mAddBeneficiarySuccessDialogVH =
            AddBeneficiarySuccessDialogVH(addBeneficiaryDialogViewClickListener)
        mAddBeneficiarySuccessDialogVH?.let { dialogVH ->
            mAddBeneficiarySuccessAlertDialog = requireActivity().showDialog(dialogVH, false) {
                PkLayoutAddBeneficiarySuccessDialogViewBinding.inflate(layoutInflater)
            }
        }
    }

    private val addBeneficiaryDialogViewClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.btnTransfer -> {
//                    mAddBeneficiarySuccessAlertDialog?.dismiss()
                    toast("Under development")
                }
                R.id.btnSendLater -> {
                    mAddBeneficiarySuccessAlertDialog?.dismiss()
                    backToBeneficiaryDashBoard()
                }
            }
        }
    }

    private fun backToBeneficiaryDashBoard() {
        requireActivity().setResult(Activity.RESULT_OK)
        finish()
    }

    private fun navigateToOtpScreen(isOtpCreated: Boolean) {
        if (isOtpCreated) {
            parentViewModel.toolBarVisibility.value = View.GONE
            navigateForResult(
                R.id.action_beneficiaryAccountDetailsFragment_to_verifyOtpFragment2,
                RESULT_CODE_OTP,
                bundleOf(KEY_OTP to OTPAction.IBFT_BENEFICIARY.name)
            )
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == RESULT_CODE_OTP) {
            viewModel.addBeneficiaryRequest(
                title = viewModel.bankAccountDetails.value?.accountHolderTitle,
                accountNo = if (viewModel.bankAccountDetails.value?.accountNumber.isNullOrBlank()) viewModel.bankAccountDetails.value?.ibanNumber else viewModel.bankAccountDetails.value?.accountNumber,
                bankName = viewModel.bankAccountDetails.value?.bankName,
                nickName = mViewBinding.etAccountHolderNickName.text.toString(),
                beneficiaryType = SendMoneyBeneficiaryType.IBFTBeneficiary.type
            )
        }
    }

    private fun handleError(errorMessage: CharSequence) {
        if (errorMessage.isEmpty().not()) {
            showTextUpdatedAbleSnackBar(
                msg = errorMessage,
                viewBgColor = R.color.pkWarningBackground,
                colorOfMessage = R.color.pkWarning,
                gravity = Gravity.TOP
            )
        } else cancelAllSnackBar()
    }

    private fun openSuccessDialog(isSuccess: Boolean) {
        if (isSuccess) {
            parentViewModel.stateList.value = viewModel.handleCompleteState()
            openAddBeneficiarySuccessDialog()
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.bankAccountDetails, ::setScreenData)
        observe(viewModel.createOtpSuccess, ::navigateToOtpScreen)
        observe(viewModel.errorDescription, ::handleError)
        observe(viewModel.addBeneficiarySuccess, ::openSuccessDialog)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAllSnackBar()
    }
}