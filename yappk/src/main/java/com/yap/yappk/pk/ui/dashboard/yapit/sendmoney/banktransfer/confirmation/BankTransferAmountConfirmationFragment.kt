package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.confirmation

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankTransferAmountConfirmationBinding
import com.yap.yappk.networking.microservices.transactions.responsedtos.BankTransferResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.success.BankTransferSuccessFragment
import com.yap.yappk.pk.utils.cancelAllSnackBar
import com.yap.yappk.pk.utils.showTextUpdatedAbleSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankTransferAmountConfirmationFragment :
    BaseNavFragment<PkFragmentBankTransferAmountConfirmationBinding, IBankTransferAmountConfirmation.State, IBankTransferAmountConfirmation.ViewModel>(
        R.layout.pk_fragment_bank_transfer_amount_confirmation
    ), IBankTransferAmountConfirmation.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IBankTransferAmountConfirmation.ViewModel by viewModels<BankTransferAmountConfirmationVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setBeneficiary(it.getParcelable(BankTransferAmountConfirmationFragment::class.java.name))
        }
    }

    private fun initViews(beneficiaryData: BankTransferDataModel) {
        mViewBinding.niView.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(beneficiaryData.beneficiary?.fullName ?: "")
                .setIndex(beneficiaryData.position ?: 0)
                .setImageUrl(beneficiaryData.beneficiary?.imgUrl)
                .build()
        )
        mViewBinding.tvBeneficiaryName.text = beneficiaryData.beneficiary?.fullName
        val firstName = beneficiaryData.beneficiary?.fullName?.split(" ")?.toMutableList()?.firstOrNull()
        viewModel.viewState.beneficiaryFirstName.value = firstName
        viewModel.viewState.transferFee.value = beneficiaryData.transactionFees
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                viewModel.bankFundsTransferRequest(
                    beneficiaryId = viewModel.beneficiary.value?.beneficiary?.beneficiaryId,
                    consumerId = null,
                    amount = viewModel.beneficiary.value?.amount,
                    purposeCode = viewModel.beneficiary.value?.selectedReason?.code,
                    purposeReason = viewModel.beneficiary.value?.selectedReason?.transferReason,
                    remarks = viewModel.beneficiary.value?.remarks, viewModel.beneficiary.value?.transactionFees
                )
            }
        }
    }

    override fun onStartIconClicked() {
        super.onStartIconClicked()
        navigateBack()
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

    private fun navigateToSuccess(bankTransferResponse: BankTransferResponse?) {
        bankTransferResponse?.let {
            viewModel.beneficiary.value.also {
                it?.bankTransferResponse = bankTransferResponse
            }
            navigate(
                R.id.action_bankTransferAmountConfirmationFragment_to_bankTransferSuccessFragment,
                bundleOf(BankTransferSuccessFragment::class.java.name to viewModel.beneficiary.value)
            )
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.beneficiary, ::initViews)
        observe(viewModel.errorDescription, ::handleError)
        observe(viewModel.bankTransferResponse, ::navigateToSuccess)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAllSnackBar()
    }
}
