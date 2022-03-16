package com.yap.yappk.pk.ui.kyc.paymentconfirmation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.extensions.getValueWithoutComa
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentCardOrderPaymentConfirmationBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_card_order_payment_confirmation_display_text_masked_number
import com.yap.yappk.localization.screen_card_order_payment_confirmation_display_text_pay_pak_scheme
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.otpverification.TopUpOtpVerificationFragment
import com.yap.yappk.pk.ui.kyc.cvv.OnBoardingCardCvvFragment
import com.yap.yappk.pk.ui.kyc.enums.CardSchemeEnum
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.*
import com.yap.yappk.pk.utils.enums.PKPartnerBankStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardOrderPaymentConfirmationFragment :
    BaseNavFragment<PkFragmentCardOrderPaymentConfirmationBinding, ICardOrderPaymentConfirmation.State, ICardOrderPaymentConfirmation.ViewModel>(
        R.layout.pk_fragment_card_order_payment_confirmation
    ), ToolBarView.OnToolBarViewClickListener, ICardOrderPaymentConfirmation.View,
    BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardOrderPaymentConfirmation.ViewModel by viewModels<CardOrderPaymentConfirmationVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
        setViewValues()
    }

    private fun setViewValues() {
        when (parentViewModel.cardScheme?.schemeName) {
            CardSchemeEnum.PayPak.scheme -> {
                mViewBinding.tvCardTile.text = requireContext().getString(
                    screen_card_order_payment_confirmation_display_text_pay_pak_scheme,
                    CardSchemeEnum.PayPak.scheme
                )
                mViewBinding.ivCardScheme.setImageResource(R.drawable.pk_spare_card_paypak)
            }
            else -> {
                mViewBinding.tvCardTile.text = requireContext().getString(
                    screen_card_order_payment_confirmation_display_text_pay_pak_scheme,
                    CardSchemeEnum.MasterCard.scheme
                )
                mViewBinding.ivCardScheme.setImageResource(R.drawable.pk_card_spare)
            }
        }
        when (parentViewModel.cardScheme?.fee ?: 0.0) {
            0.0 -> mViewBinding.clPaymentView.gone()
            else -> setPaymentViewValues()
        }
    }

    private fun setPaymentViewValues() {
        mViewBinding.tvMaskedCardNumber.text = requireContext().getString(
            screen_card_order_payment_confirmation_display_text_masked_number,
            parentViewModel.externalCard?.number?.takeLast(4) ?: ""
        )
        mViewBinding.ivPayCardScheme.setImageDrawable(
            parentViewModel.externalCard?.getCardLogoByType(
                requireContext(),
                true
            )
        )
    }

    override fun getFragmentArguments() {
        viewModel.setAddress(arguments?.getParcelable(CardOrderPaymentConfirmationFragment::class.java.name))
        viewModel.viewState.orderNewCardFee.value =
            parentViewModel.cardScheme?.fee.toString()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvLocationEdit -> {
                navigateBack()
            }
            R.id.btnConfirmOrder -> {
                createCardRoute()
            }
        }
    }

    private fun createCardRoute() {
        when {
            (viewModel.sessionManager.userAccount.value?.isFirstCredit == true || parentViewModel.cardScheme?.fee ?: 0.0 == 0.0) &&
                    (viewModel.sessionManager.userAccount.value?.partnerBankStatus ?: "" == PKPartnerBankStatus.IBAN_ASSIGNED.name
                            || viewModel.sessionManager.userAccount.value?.partnerBankStatus ?: "" == PKPartnerBankStatus.PHYSICAL_CARD_PENDING.name) -> {
                viewModel.orderCard(
                    (parentViewModel.cardScheme?.fee ?: 0).toString(),
                    parentViewModel.cardScheme?.schemeName ?: ""
                )
            }
            parentViewModel.cardScheme?.fee ?: 0.0 > 0.0 && (viewModel.sessionManager.userAccount.value?.partnerBankStatus ?: "" == PKPartnerBankStatus.IBAN_ASSIGNED.name
                    || viewModel.sessionManager.userAccount.value?.partnerBankStatus ?: "" == PKPartnerBankStatus.PHYSICAL_CARD_PENDING.name) -> {
                createSessionRequestRoute()
            }
            else -> {
                viewModel.saveAddressCard(parentViewModel.cardDeliveryAddress ?: CardOrderRequest())
            }
        }
    }

    private fun createSessionRequestRoute() {
        if (parentViewModel.externalCard?.sessionId.isNullOrBlank()) {
            viewModel.createTopUpSessionUseCase(
                sessionId = null,
                beneficiaryId = if (parentViewModel.externalCard?.id.isNullOrBlank()) null
                else parentViewModel.externalCard?.id?.parseToInt(),
                currency = viewModel.sessionManager.userAccount.value?.currency?.code ?: "PKR",
                amount = viewModel.viewState.orderNewCardFee.value
            )
        } else viewModel.createTopUpSessionUseCase(
            sessionId = parentViewModel.externalCard?.sessionId,
            beneficiaryId = null,
            currency = viewModel.sessionManager.userAccount.value?.currency?.code ?: "PKR",
            amount = viewModel.viewState.orderNewCardFee.value
        )
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    private fun handleAddressState(address: CardOrderRequest) {
        viewModel.viewState.cardAddressTitle?.value = address.address1
        viewModel.viewState.cardAddressSubTitle?.value = address.address2
    }

    private fun navigateToTopUpOtpVerification(check3DEnrollmentSession: Check3DEnrollmentSession?) {
        viewModel.setSecureId(check3DEnrollmentSession?._3DSecureId)
        navigateForResult(
            R.id.action_cardOrderPaymentConfirmationFragment_to_topUpOtpVerificationFragment2,
            requestCode = RESULT_CODE_TOP_UP,
            bundleOf(TopUpOtpVerificationFragment::class.java.name to check3DEnrollmentSession?._3DSecure?.authenticationRedirect?.simple?.htmlBodyContent)
        )
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == RESULT_CODE_TOP_UP && result.resultCode == Activity.RESULT_OK
        ) {
            startPooling(viewModel.secureId.value)
        } else if (result.resultCode == RESULT_CODE_TOP_UP_CVV) {
            result.data?.getString(KEY_TOP_UP_CVV)?.let { cardCvv ->
                topUpRequest(cardCvv)
            }
        }
    }

    private fun topUpRequest(cvv: String?) {
        viewModel.topUpTransactionRequest(
            beneficiaryId = if (parentViewModel.externalCard?.id.isNullOrBlank()) null else parentViewModel.externalCard?.id,
            secureId = viewModel.secureId.value ?: "",
            orderId = viewModel.orderId.value ?: "",
            currency = viewModel.sessionManager.userAccount.value?.currency?.code ?: "PKR",
            amount = parentViewModel.cardScheme?.fee.toString(),
            securityCode = cvv,
            sessionId = parentViewModel.externalCard?.sessionId
        )
    }

    private fun startPooling(secureId: String?) {
        viewModel.startPooling(secureId)
    }

    private fun navigateToCardCVV(isSuccess: Boolean) {
        if (isSuccess) {
            if (parentViewModel.externalCard?.sessionId.isNullOrEmpty())
                navigateForResult(
                    resId = R.id.action_cardOrderPaymentConfirmationFragment_to_onBoardingCardCvvFragment,
                    requestCode = RESULT_CODE_TOP_UP_CVV,
                    bundleOf(
                        OnBoardingCardCvvFragment::class.java.name to parentViewModel.externalCard,
                        KEY to viewModel.viewState.orderNewCardFee.value.getValueWithoutComa(),
                        KEY_SECURE_ID to viewModel.secureId.value,
                        KEY_ORDER_ID to viewModel.orderId.value
                    )
                )
            else {
                viewModel.hideLoading()
                topUpRequest(null)
            }
        } else startPooling(viewModel.secureId.value)
    }

    private fun orderCard(isSuccess: Boolean) {
        //call save address api
        viewModel.orderCard(
            (parentViewModel.cardScheme?.fee ?: 0).toString(),
            parentViewModel.cardScheme?.schemeName ?: ""
        )
    }

    private fun handleSaveAddressCard(accountInfo: AccountInfo?) {
        accountInfo?.let { account ->
            viewModel.sessionManager.updateAccountData(accountInfo)
            createCardRoute()
        }
    }

    private fun handleOrderedCard(accountInfo: AccountInfo?) {
        accountInfo?.let { account ->
            viewModel.sessionManager.updateAccountData(accountInfo)
            if (account.isSecretQuestionVerified == true)
                viewModel.openCardOrderSuccessScreen()
            else
                viewModel.openValidCustomerScreen()
        }
    }

    private fun openSuccessScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let {
            navigate(it)
        }
    }

    private fun openValidScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let {
            navigate(it)
        }
    }

    private fun openInValidScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let {
            navigate(it)
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.address, ::handleAddressState)
        observe(viewModel.check3dSecureSuccess, ::navigateToTopUpOtpVerification)
        observe(viewModel.poolingSuccess, ::navigateToCardCVV)
        observe(viewModel.topUpSuccess, ::orderCard)
        observe(viewModel.accountInfo, ::handleSaveAddressCard)
        observe(viewModel.isCardOrdered, ::handleOrderedCard)
        observeEvent(viewModel.openCardOrderSuccess, ::openSuccessScreen)
        observeEvent(viewModel.openInValidCustomer, ::openValidScreen)
        observeEvent(viewModel.openValidCustomer, ::openInValidScreen)
    }
}
