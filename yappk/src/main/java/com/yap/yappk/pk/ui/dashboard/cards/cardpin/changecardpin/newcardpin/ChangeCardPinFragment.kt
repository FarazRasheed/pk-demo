package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.newcardpin

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
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
import com.yap.yappk.databinding.PkFragmentChangeCardPinBinding
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangeCardPinKeys.CHANGE_PIN_MODEL
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangeCardPinKeys.SCREEN_TYPE_KEY
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType.SCREEN_TYPE_CHANGE_CONFIRM_PIN
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType.SCREEN_TYPE_CHANGE_PIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeCardPinFragment :
    BaseNavFragment<PkFragmentChangeCardPinBinding, IChangeCardPin.State, IChangeCardPin.ViewModel>(
        R.layout.pk_fragment_change_card_pin
    ), IChangeCardPin.View, ToolBarView.OnToolBarViewClickListener,
    KeyClickListener,
    OnSecureCodeListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IChangeCardPin.ViewModel by viewModels<ChangeCardPinVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.changeCardPinModel.value =
            arguments?.getParcelable(CHANGE_PIN_MODEL.name)
        viewModel.viewState.screenType.value = arguments?.getString(SCREEN_TYPE_KEY.name)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initPinCodeView()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                viewModel.checkPasscodeValidation()
            }
        }
    }

    private fun initPinCodeView() {
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

    override fun onStartIconClicked() {
        super.onStartIconClicked()
        navigateBack()
    }

    private fun handlePinError(errorMsg: String) {
        val oldPin = viewModel.viewState.changeCardPinModel.value?.oldCardPin ?: ""
        val newCardPin = viewModel.viewState.changeCardPinModel.value?.cardConfirmPin ?: ""
        val confirmCardPin = viewModel.viewState.pin.value ?: ""
        if (errorMsg.isBlank()) {
            when (viewModel.viewState.screenType.value) {
                SCREEN_TYPE_CHANGE_PIN.name -> {
                    viewModel.navigateToConfirmCardPinScreen()
                }
                SCREEN_TYPE_CHANGE_CONFIRM_PIN.name -> {
                    viewModel.changeCardPin(
                        oldPin,
                        newCardPin,
                        confirmCardPin,
                        parentViewModel.card?.cardSerialNumber ?: ""
                    )
                }
            }
        }
    }

    private fun navigateToSuccess(isSuccess: Boolean) {
        if (isSuccess) viewModel.navigateToChangeCardPinSuccessScreen()
    }

    private fun openConfirmCardPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            viewModel.viewState.changeCardPinModel.value?.also {
                it.screenType = SCREEN_TYPE_CHANGE_CONFIRM_PIN
                it.cardConfirmPin = viewModel.viewState.pin.value
            }
            navigate(
                destinationId,
                bundleOf(
                    CHANGE_PIN_MODEL.name to viewModel.viewState.changeCardPinModel.value,
                    SCREEN_TYPE_KEY.name to SCREEN_TYPE_CHANGE_CONFIRM_PIN.name
                )
            )
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.viewState.pinError, ::handlePinError)
        observe(viewModel.changePinSuccess, ::navigateToSuccess)
        observeEvent(viewModel.openConfirmCardPin, ::openConfirmCardPinScreen)
    }

}
