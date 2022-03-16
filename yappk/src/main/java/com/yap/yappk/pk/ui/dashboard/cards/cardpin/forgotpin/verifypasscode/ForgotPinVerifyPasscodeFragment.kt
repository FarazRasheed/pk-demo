package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.verifypasscode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
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
import com.yap.yappk.databinding.FragmentForgotPinVerifyPasscodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPinVerifyPasscodeFragment :
    BaseNavFragment<FragmentForgotPinVerifyPasscodeBinding, IForgotPinVerifyPasscode.State, IForgotPinVerifyPasscode.ViewModel>(
        R.layout.fragment_forgot_pin_verify_passcode
    ),
    IForgotPinVerifyPasscode.View, ToolBarView.OnToolBarViewClickListener, KeyClickListener,
    OnSecureCodeListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IForgotPinVerifyPasscode.ViewModel by viewModels<ForgotPinVerifyPasscodeVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                viewModel.handlePressOnVerify()
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
    }

    private fun initKeyBoardView() {
        mViewBinding.keyBoard.setKeyClickListener(this)
        mViewBinding.keyBoard.setOnSecureCodeListener(this)
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun onKeyClicked(view: View, which: KeyEvent) {
        when (which) {
            KeyEvent.LEFT -> {
                mViewBinding.keyBoard.delete()
                mViewBinding.tvPassCode.delete()
                viewModel.viewState.passcodeError.value = null
            }
            else -> {
                mViewBinding.keyBoard.input(which.value)
                mViewBinding.tvPassCode.input(which.value)
            }
        }
    }

    override fun onCodeCompleted(code: String?, isCompleted: Boolean) {
        super.onCodeCompleted(code, isCompleted)
        viewModel.viewState.passcode.value = code
        viewModel.viewState.valid.value = isCompleted
    }

    override fun onCodeChange(code: String?) {
        super.onCodeChange(code)
        viewModel.viewState.passcodeError.value = null
        viewModel.viewState.passcode.value = code
    }

    private fun openEnterNewCardPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(destinationId, R.id.forgotPinVerifyPasscodeFragment)
        }
    }

    private fun handlePasscodeVerified(isPasscodeVerified: Boolean) {
        if (isPasscodeVerified) {
            viewModel.openEnterNewCardPinScreen()
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.isPasscodeVerified, ::handlePasscodeVerified)
        observeEvent(viewModel.openEnterNewCardPinScreen, ::openEnterNewCardPinScreen)
    }

}