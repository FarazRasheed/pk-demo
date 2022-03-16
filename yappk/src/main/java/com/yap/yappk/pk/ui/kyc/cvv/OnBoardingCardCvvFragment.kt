package com.yap.yappk.pk.ui.kyc.cvv

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
import com.yap.yappk.databinding.PkFragmentOnboardingCardCvvBinding
import com.yap.yappk.localization.common_masked_card_number
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.utils.KEY
import com.yap.yappk.pk.utils.KEY_TOP_UP_CVV
import com.yap.yappk.pk.utils.RESULT_CODE_TOP_UP
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class OnBoardingCardCvvFragment :
    BaseNavFragment<PkFragmentOnboardingCardCvvBinding, IOnBoardingCardCvv.State, IOnBoardingCardCvv.ViewModel>(
        R.layout.pk_fragment_onboarding_card_cvv
    ), IOnBoardingCardCvv.View, ToolBarView.OnToolBarViewClickListener, OTPListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IOnBoardingCardCvv.ViewModel by viewModels<OnBoardingCardCvvVM>()

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
            viewModel.setCard(it.getParcelable(OnBoardingCardCvvFragment::class.java.name))
            viewModel.setTopUpAmount(it.getString(KEY))
        }
    }

    private fun handleCard(externalCard: ExternalCard?) {
        externalCard?.let {
            mViewBinding.ivCustomCard.paymentCardBackgroundColor = externalCard.getCardColor(requireContext())
            mViewBinding.ivCustomCard.paymentCardNumber =
                requireContext().getString(common_masked_card_number, externalCard.number?.takeLast(4) ?: "")
            mViewBinding.ivCustomCard.paymentCardDate = externalCard.getCardExpiry(requireContext())
            mViewBinding.ivCustomCard.paymentCardLogoIcon = externalCard.getCardLogoByType(requireContext())
            mViewBinding.ivCustomCard.paymentCardName = externalCard.alias
            mViewBinding.tvCardNumber.text =
                requireContext().getString(common_masked_card_number, externalCard.number?.takeLast(4) ?: "")
            mViewBinding.tvCardName.text = externalCard.alias
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                navigateBack(viewModel.viewState.cvv.value)
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
        navigateBack(otp)
    }

    private fun navigateBack(cardCvv: String?) {
        navigateBackWithResult(
            resultCode = RESULT_CODE_TOP_UP,
            bundleOf(KEY_TOP_UP_CVV to cardCvv)
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.externalCard, ::handleCard)
    }
}
