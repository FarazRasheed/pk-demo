package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.cardcvv

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.showKeyboard
import com.yap.uikit.widget.otptextview.OTPListener
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentCardCvvBinding
import com.yap.yappk.localization.common_masked_card_number
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer.TopUpViaExternalCardFragment
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.topupsuccess.TopUpSuccessFragment
import com.yap.yappk.pk.utils.KEY
import com.yap.yappk.pk.utils.KEY_ORDER_ID
import com.yap.yappk.pk.utils.KEY_SECURE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CardCvvFragment :
    BaseNavFragment<PkFragmentCardCvvBinding, ICardCvv.State, ICardCvv.ViewModel>(
        R.layout.pk_fragment_card_cvv
    ), ICardCvv.View, ToolBarView.OnToolBarViewClickListener, OTPListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardCvv.ViewModel by viewModels<CardCvvVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.cvvView.otpListener = this
        getFragmentArguments()
        launch(Dispatcher.Main) {
            delay(200)
            mViewBinding.cvvView[0].showKeyboard()
        }
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setCard(it.getParcelable(TopUpViaExternalCardFragment::class.java.name))
            viewModel.setTopUpAmount(it.getString(KEY))
            viewModel.setSecureId(it.getString(KEY_SECURE_ID))
            viewModel.setOrderId(it.getString(KEY_ORDER_ID))
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

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                topUpRequest(viewModel.viewState.cvv.value ?: "")
            }
        }
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun onInteractionListener() {

    }

    override fun onOTPComplete(otp: String) {
        viewModel.viewState.cvv.value = otp
        topUpRequest(otp)
    }

    private fun topUpRequest(cvv: String) {
        viewModel.topUpTransactionRequest(
            beneficiaryId = viewModel.externalCard.value?.id ?: "",
            securityCode = cvv,
            amount = viewModel.topUpAmount.value ?: "",
            secureId = viewModel.secureId.value ?: "",
            currency = viewModel.sessionManager.userAccount.value?.currency?.code ?: "",
            orderId = viewModel.orderId.value ?: ""
        )
    }

    private fun navigateToTopUpSuccess(isSuccess: Boolean) {
        if (isSuccess) navigate(
            R.id.action_cardCvvFragment_to_topUpSuccessFragment,
            bundleOf(
                TopUpSuccessFragment::class.java.name to viewModel.externalCard.value,
                KEY to viewModel.topUpAmount.value
            )
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.externalCard, ::handleCard)
        observe(viewModel.topUpSuccess, ::navigateToTopUpSuccess)

    }
}
