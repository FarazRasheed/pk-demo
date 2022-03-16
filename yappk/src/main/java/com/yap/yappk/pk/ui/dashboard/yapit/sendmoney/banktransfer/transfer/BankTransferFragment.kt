package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.extensions.hideKeyboard
import com.yap.uikit.utils.extensions.showKeyboard
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankTransferBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferReasons
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.bottomsheetadapter.PrimaryCardDetailMoreViewAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.confirmation.BankTransferAmountConfirmationFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import com.yap.yappk.pk.utils.KEY
import com.yap.yappk.pk.utils.cancelAllSnackBar
import com.yap.yappk.pk.utils.showTextUpdatedAbleSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class BankTransferFragment :
    BaseNavFragment<PkFragmentBankTransferBinding, IBankTransfer.State, IBankTransfer.ViewModel>(
        R.layout.pk_fragment_bank_transfer
    ), IBankTransfer.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IBankTransfer.ViewModel by viewModels<BankTransferVM>()

    lateinit var adapter: PrimaryCardDetailMoreViewAdapter
    var chooseBottomDialog: BottomSheetUIDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllInitialApis()
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
        initListeners()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setBeneficiary(
                BankTransferDataModel(
                    beneficiary = it.getParcelable(BankTransferFragment::class.java.name),
                    position = it.getInt(
                        KEY
                    )
                )
            )
        }
    }

    override fun initListeners() {
        mViewBinding.tlReasons.setEndIconOnClickListener {
            chooseBottomDialog?.show(childFragmentManager, "Choose")
        }
    }

    private val chooseItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            chooseBottomDialog?.dismiss()
            viewModel.beneficiary.value.also {
                it?.selectedReason = viewModel.reasonsList.value?.get(pos)
            }
            mViewBinding.etReasons.setText(viewModel.beneficiary.value?.selectedReason?.transferReason)
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
        val accountTitle =
            if (beneficiaryData.beneficiary?.accountNumber.isNullOrBlank()) beneficiaryData.beneficiary?.ibanNumber else beneficiaryData.beneficiary?.accountNumber
        mViewBinding.tvAccountNumber.text = accountTitle
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                navigate(
                    R.id.action_bankTransferFragment_to_bankTransferAmountConfirmationFragment, bundleOf(
                        BankTransferAmountConfirmationFragment::class.java.name to viewModel.beneficiary.value?.also {
                            it.transactionFees = viewModel.viewState.transferFee.value
                            it.amount = mViewBinding.etAmount.text.toString()
                            it.remarks = mViewBinding.etTransferNote.text.toString()
                        }
                    )
                )
            }
        }
    }

    private fun updateScreen(isFocusScreen: Boolean) {
        if (isFocusScreen) {
            launch(Dispatcher.Main) {
                delay(200)
                mViewBinding.etAmount.showKeyboard()
            }
        }
    }

    override fun onEndTextClicked() {
        super.onEndTextClicked()
        showCancelAlert()
    }

    private fun showCancelAlert() {
        confirm(
            title = requireContext().getString(common_text_exit_pop_up_heading),
            message = requireContext().getString(
                common_text_exit_pop_up_description
            ),
            positiveButton = requireContext().getString(common_button_confirm),
            negativeButton = requireContext().getString(common_button_cancel),
            cancelable = false
        ) {
            mViewBinding.etAmount.hideKeyboard()
            navigateBack()
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

    private fun setReasonsList(reasonsList: ArrayList<FundTransferReasons?>) {
        chooseBottomDialog = getBottomSheetBuilder(reasonsList)
    }

    private fun getBottomSheetBuilder(reasonsList: ArrayList<FundTransferReasons?>): BottomSheetUIDialog? {
        adapter =
            PrimaryCardDetailMoreViewAdapter(
                reasonsList.map { it?.transferReason ?: "" }.toMutableList(),
                chooseItemClickListener
            )
        val builder =
            BottomSheetBuilder().setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        return builder.build()
    }

    override fun viewModelObservers() {
        observe(viewModel.beneficiary, ::initViews)
        observe(viewModel.viewState.isFocusable, ::updateScreen)
        observe(viewModel.errorDescription, ::handleError)
        observe(viewModel.reasonsList, ::setReasonsList)
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelAllSnackBar()
    }
}
