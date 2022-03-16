package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.extensions.observe
import com.digitify.core.utils.KEY_APP_UUID
import com.yap.uikit.utils.extensions.hideKeyboard
import com.yap.uikit.utils.extensions.showKeyboard
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentYapToYapTransferBinding
import com.yap.yappk.networking.microservices.transactions.responsedtos.YapToYapTransaction
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.success.YapToYapTransferSuccessFragment
import com.yap.yappk.pk.utils.YAP_TO_YAP_SUCCESS_MODEL
import com.yap.yappk.pk.utils.cancelAllSnackBar
import com.yap.yappk.pk.utils.showTextUpdatedAbleSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class YapToYapTransferFragment :
    BaseNavFragment<PkFragmentYapToYapTransferBinding, IYapToYapTransfer.State, IYapToYapTransfer.ViewModel>(
        R.layout.pk_fragment_yap_to_yap_transfer
    ), IYapToYapTransfer.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IYapToYapTransfer.ViewModel by viewModels<YapToYapTransferVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.contact.value =
            arguments?.getParcelable(YapToYapTransferFragment::class.java.name)
        viewModel.getCombinedApisParallel()
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initViews()
    }

    private fun initViews() {
        mViewBinding.tvMobileNumber.text = getFormattedPhoneNumber(
            requireContext(),
            viewModel.viewState.contact.value?.countryCde?.replace(
                "+",
                "00"
            ) + viewModel.viewState.contact.value?.mobileNumber
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                viewModel.y2yTransferRequest(
                    receiverUUID = viewModel.viewState.contact.value?.accountUUID,
                    beneficiaryName = viewModel.viewState.contact.value?.fullName,
                    viewModel.sharedPreferenceManager.getValueString(KEY_APP_UUID),
                    amount = mViewBinding.etAmount.text.toString(),
                    otpVerificationReq = false,
                    remarks = mViewBinding.etTransferNote.text.toString()
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

    private fun navigateToSuccess(successModel: YapToYapTransaction) {
        navigate(
            R.id.action_yapToYapTransferFragment_to_yapToYapTransferSuccessFragment2,
            bundleOf(
                YapToYapTransferSuccessFragment::class.java.name to viewModel.viewState.contact.value,
                YAP_TO_YAP_SUCCESS_MODEL to successModel
            )
        )
    }

    override fun onStartIconClicked() {
        mViewBinding.etAmount.hideKeyboard()
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

    override fun onDestroy() {
        super.onDestroy()
        cancelAllSnackBar()
    }

    override fun viewModelObservers() {
        observe(viewModel.viewState.isFocusable, ::updateScreen)
        observe(viewModel.y2yTransferSuccess, ::navigateToSuccess)
        observe(viewModel.errorDescription, ::handleError)
    }
}
