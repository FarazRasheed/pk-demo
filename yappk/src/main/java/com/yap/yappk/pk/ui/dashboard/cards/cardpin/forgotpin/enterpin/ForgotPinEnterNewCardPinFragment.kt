package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.enterpin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.dialerpad.KeyClickListener
import com.yap.uikit.widget.dialerpad.KeyEvent
import com.yap.uikit.widget.dialerpad.OnSecureCodeListener
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentForgotPinEnterNewCardPinBinding
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.generic.enums.OTPAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPinEnterNewCardPinFragment :
    BaseNavFragment<FragmentForgotPinEnterNewCardPinBinding, IForgotPinEnterNewCardPin.State, IForgotPinEnterNewCardPin.ViewModel>(
        R.layout.fragment_forgot_pin_enter_new_card_pin
    ),
    IForgotPinEnterNewCardPin.View, ToolBarView.OnToolBarViewClickListener, KeyClickListener,
    OnSecureCodeListener, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IForgotPinEnterNewCardPin.ViewModel by viewModels<ForgotPinEnterNewCardPinVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                if (viewModel.viewState.isConfirmedPin.value == true) viewModel.handlePressOnCreate() else viewModel.handlePressOnNext()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initKeyBoardView()
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        viewModel.viewState.isConfirmedPin.value = arguments?.let {
            viewModel.confirmPin =
                it.getString(ForgotPinEnterNewCardPinFragment::class.java.name) ?: ""
            !viewModel.confirmPin.isNullOrEmpty()
        }
    }

    private fun initKeyBoardView() {
        mViewBinding.keyBoard.setKeyClickListener(this)
        mViewBinding.keyBoard.setOnSecureCodeListener(this)
        setValues()
    }

    private fun setValues() {
        if (!viewModel.viewState.pin.value.isNullOrBlank()) {
            viewModel.viewState.pin.value?.forEach {
                mViewBinding.tvPassCode.input(it.toString())
                mViewBinding.keyBoard.input(it.toString())
            }
        }
    }

    override fun onKeyClicked(view: View, which: KeyEvent) {
        when (which) {
            KeyEvent.LEFT -> {
                mViewBinding.keyBoard.delete()
                mViewBinding.tvPassCode.delete()
                viewModel.viewState.pinError.value = null
            }
            else -> {
                mViewBinding.keyBoard.input(which.value)
                mViewBinding.tvPassCode.input(which.value)
            }
        }
    }

    override fun onCodeCompleted(code: String?, isCompleted: Boolean) {
        super.onCodeCompleted(code, isCompleted)
        viewModel.viewState.pin.value = code
        viewModel.viewState.valid.value = isCompleted
    }

    override fun onCodeChange(code: String?) {
        super.onCodeChange(code)
        viewModel.viewState.pinError.value = null
        viewModel.viewState.pin.value = code
    }

    private fun handlePinError(errorMsg: String) {
        if (errorMsg.isBlank()) {
            if (viewModel.viewState.isConfirmedPin.value == true) viewModel.createOtpForgotCardPin(
                OTPAction.FORGOT_CARD_PIN.name
            )
            else viewModel.openCardConfirmPinScreen()
        }
    }

    private fun openCardConfirmPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            val bundle = Bundle()
            bundle.putString(
                ForgotPinEnterNewCardPinFragment::class.java.name,
                viewModel.viewState.pin.value
            )
            navigateForResult(destinationId, 100, bundle)
        }
    }

    private fun openVerifyOtpScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            val bundle = Bundle()
            bundle.putString(
                "otp_action",
                OTPAction.FORGOT_CARD_PIN.name
            )
            navigateForResult(destinationId, 200, bundle)
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    private fun handleForgotCardPinCreated(isForgotPinOtpCreated: Boolean) {
        if (isForgotPinOtpCreated) {
            viewModel.openVerifyOtpScreen()
        }
    }

    private fun handleCardPinCreated(isPinOtpCreated: Boolean) {
        if (isPinOtpCreated) {
            viewModel.openForgotCardPinSuccessScreen()
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == 200) {
            val token = result.data?.getString("otp_token")
            callCreatePinAPI(token)
        } else {
            navigateBackWithResult(100)
        }
    }

    private fun callCreatePinAPI(token: String?) {
        viewModel.createForgotCardPin(
            cardSerialNumber = parentViewModel.card?.cardSerialNumber ?: "",
            newPin = viewModel.viewState.pin.value ?: "",
            otpToken = token ?: ""
        )
    }

    private fun openForgotCardPinSuccessScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateForResult(destinationId, 100)
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.viewState.pinError, ::handlePinError)
        observe(viewModel.isForgotCardPinOtpCreated, ::handleForgotCardPinCreated)
        observe(viewModel.isCardPinCreated, ::handleCardPinCreated)
        observeEvent(viewModel.openConfirmPin, ::openCardConfirmPinScreen)
        observeEvent(viewModel.openVerifyOtp, ::openVerifyOtpScreen)
        observeEvent(viewModel.openForgotCardPinSuccess, ::openForgotCardPinSuccessScreen)
    }

}