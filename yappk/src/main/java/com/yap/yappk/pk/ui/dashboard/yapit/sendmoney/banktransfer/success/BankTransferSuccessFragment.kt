package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finish
import com.digitify.core.extensions.observe
import com.digitify.core.utils.DateUtils
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankTransferSuccessBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_bank_transfer_success_display_text_account_number
import com.yap.yappk.localization.screen_bank_transfer_success_display_text_iban_number
import com.yap.yappk.localization.screen_bank_transfer_success_display_text_reference_number
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import com.yap.yappk.pk.utils.shareImage
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BankTransferSuccessFragment :
    BaseNavFragment<PkFragmentBankTransferSuccessBinding, IBankTransferSuccess.State, IBankTransferSuccess.ViewModel>(
        R.layout.pk_fragment_bank_transfer_success
    ), IBankTransferSuccess.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IBankTransferSuccess.ViewModel by viewModels<BankTransferSuccessVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setBeneficiary(it.getParcelable(BankTransferSuccessFragment::class.java.name))
        }
    }

    private fun initViews(bankTransferDataModel: BankTransferDataModel) {
        mViewBinding.niView.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankTransferDataModel.beneficiary?.fullName ?: "")
                .setIndex(bankTransferDataModel.position ?: 0)
                .setImageUrl(bankTransferDataModel.beneficiary?.imgUrl)
                .build()
        )
        mViewBinding.ivBankLogo.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankTransferDataModel.beneficiary?.bankTitle ?: "")
                .setIndex(bankTransferDataModel.position ?: 0)
                .setImageUrl(bankTransferDataModel.beneficiary?.bankImgUrl)
                .build()
        )
        mViewBinding.tvBankName.text = bankTransferDataModel.beneficiary?.bankTitle
        mViewBinding.tvReceiverName.text = bankTransferDataModel.beneficiary?.fullName
        mViewBinding.tvTransferAmountValue.text =
            bankTransferDataModel.bankTransferResponse?.amountTransferred.toFormattedCurrency()
        mViewBinding.tvReceiverAccountNumber.text =
            if (bankTransferDataModel.bankTransferResponse?.accountNo?.startsWith("PK") == true) requireContext().getString(
                screen_bank_transfer_success_display_text_iban_number,
                bankTransferDataModel.bankTransferResponse?.accountNo ?: ""
            ) else requireContext().getString(
                screen_bank_transfer_success_display_text_account_number,
                bankTransferDataModel.bankTransferResponse?.accountNo ?: ""
            )
        mViewBinding.tvReferenceNumber.text = requireContext().getString(
            screen_bank_transfer_success_display_text_reference_number,
            bankTransferDataModel.bankTransferResponse?.transactionId ?: ""
        )
        val date = DateUtils.reformatStringDate(
            bankTransferDataModel.bankTransferResponse?.date ?: "",
            DateUtils.SERVER_DATE_FORMAT,
            DateUtils.FORMAT_LONG_OUTPUT
        )
        mViewBinding.tvDate.text = date
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnGoToDashboard -> {
                this.finish()
            }
        }
    }

    override fun onEndIconClicked() {
        context?.shareImage(
            mViewBinding.clShare,
            imageName = "shareYapPkImageName",
            shareText = "",
            chooserTitle = "",
            applicationId = pkBuildConfigurations.configManager?.applicationId ?: ""
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.beneficiary, ::initViews)
    }
}
