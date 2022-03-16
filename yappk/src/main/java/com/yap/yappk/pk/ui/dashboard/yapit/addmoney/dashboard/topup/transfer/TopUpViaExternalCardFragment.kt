package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.extensions.getValueWithoutComa
import com.yap.uikit.utils.extensions.hideKeyboard
import com.yap.uikit.utils.extensions.showKeyboard
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentTopUpViaExternalCardBinding
import com.yap.yappk.localization.common_masked_card_number
import com.yap.yappk.localization.common_text_currency_type
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferDenominations
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.otpverification.TopUpOtpVerificationFragment
import com.yap.yappk.pk.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class TopUpViaExternalCardFragment :
    BaseNavFragment<PkFragmentTopUpViaExternalCardBinding, ITopUpViaExternalCard.State, ITopUpViaExternalCard.ViewModel>(
        R.layout.pk_fragment_top_up_via_external_card
    ), ITopUpViaExternalCard.View, ToolBarView.OnToolBarViewClickListener, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ITopUpViaExternalCard.ViewModel by viewModels<TopUpViaExternalCardVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.fetchAllInitialApis()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        viewModel.denominationsAdapter.onItemClickListener = itemClickListener
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setCard(it.getParcelable(TopUpViaExternalCardFragment::class.java.name))
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                viewModel.createTransactionSession(
                    requireContext().getString(common_text_currency_type),
                    mViewBinding.etAmount.text.toString().getValueWithoutComa()
                )
            }
        }
    }

    override fun onStartIconClicked() {
        mViewBinding.etAmount.hideKeyboard()
        navigateBack()
    }

    private val itemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is FundTransferDenominations) {
                mViewBinding.etAmount.hideKeyboard()
                mViewBinding.etAmount.setText("")
                mViewBinding.etAmount.append(data.amount)
                mViewBinding.etAmount.clearFocus()
            }
        }
    }

    private fun handleCard(externalCard: ExternalCard?) {
        externalCard?.let {
            mViewBinding.ivCustomCard.paymentCardBackgroundColor = externalCard.getCardColor(requireContext())
            mViewBinding.ivCustomCard.paymentCardNumber =
                requireContext().getString(common_masked_card_number, externalCard.number ?: "")
            mViewBinding.ivCustomCard.paymentCardDate = externalCard.getCardExpiry(requireContext())
            mViewBinding.ivCustomCard.paymentCardLogoIcon = externalCard.getCardLogoByType(requireContext())
            mViewBinding.ivCustomCard.paymentCardName = externalCard.alias
            mViewBinding.tvCardNumber.text =
                requireContext().getString(common_masked_card_number, externalCard.number ?: "")
            mViewBinding.tvCardName.text = externalCard.alias
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

    fun createOtp(createSession: CreateSession) {
        viewModel.setOrderId(createSession.order?.id)
        viewModel.check3DEnrollmentSessionRequest(
            beneficiaryId = viewModel.externalCard.value?.id?.parseToInt(),
            orderId = createSession.order?.id,
            sessionId = createSession.session?.id,
            requireContext().getString(common_text_currency_type),
            mViewBinding.etAmount.text.toString().getValueWithoutComa()
        )
    }

    private fun navigateToTopUpOtpVerification(check3DEnrollmentSession: Check3DEnrollmentSession) {
        viewModel.setSecureId(check3DEnrollmentSession._3DSecureId)
        navigateForResult(
            R.id.action_topUpViaExternalCardFragment_to_topUpOtpVerificationFragment,
            requestCode = RESULT_CODE_TOP_UP,
            bundleOf(TopUpOtpVerificationFragment::class.java.name to check3DEnrollmentSession._3DSecure.authenticationRedirect.simple?.htmlBodyContent)
        )
    }

    private fun startPooling(secureId: String?) {
        viewModel.startPooling(secureId)
    }

    private fun navigateToCardCVV(isSuccess: Boolean) {
        if (isSuccess) navigate(
            R.id.action_topUpViaExternalCardFragment_to_cardCvvFragment,
            bundleOf(
                TopUpViaExternalCardFragment::class.java.name to viewModel.externalCard.value,
                KEY to mViewBinding.etAmount.text.toString().getValueWithoutComa(),
                KEY_SECURE_ID to viewModel.secureId.value,
                KEY_ORDER_ID to viewModel.orderId.value
            )
        ) else startPooling(viewModel.secureId.value)
    }

    override fun viewModelObservers() {
        observe(viewModel.externalCard, ::handleCard)
        observe(viewModel.viewState.isFocusable, ::updateScreen)
        observe(viewModel.errorDescription, ::handleError)
        observe(viewModel.checkOutSessionResponse, ::createOtp)
        observe(viewModel.check3dSecureSuccess, ::navigateToTopUpOtpVerification)
        observe(viewModel.poolingSuccess, ::navigateToCardCVV)
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == RESULT_CODE_TOP_UP && result.resultCode == Activity.RESULT_OK
        ) {
            startPooling(viewModel.secureId.value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAllSnackBar()
    }
}

