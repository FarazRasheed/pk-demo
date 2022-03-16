package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.currentcardpin

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
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
import com.yap.yappk.databinding.PkFragmentCurrentCardPinBinding
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangeCardPinKeys.CHANGE_PIN_MODEL
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangeCardPinKeys.SCREEN_TYPE_KEY
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType.SCREEN_TYPE_CHANGE_PIN
import com.yap.yappk.pk.ui.dashboard.cards.models.ChangeCardPinExtras
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentCardPinFragment :
    BaseNavFragment<PkFragmentCurrentCardPinBinding, ICurrentCardPin.State, ICurrentCardPin.ViewModel>(
        R.layout.pk_fragment_current_card_pin
    ), ICurrentCardPin.View, ToolBarView.OnToolBarViewClickListener,
    KeyClickListener,
    OnSecureCodeListener, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ICurrentCardPin.ViewModel by viewModels<CurrentCardPinVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initPinCodeView()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                viewModel.verifyCardPin(
                    cardPin = viewModel.viewState.pin.value ?: "",
                    cardSerialNumber = parentViewModel.card?.cardSerialNumber ?: ""
                )
            }
            R.id.tvForgotPin -> {
                viewModel.navigateToForgotPinScreen()
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
        requireActivity().onBackPressed()
    }

    private fun navigateNext(success: Boolean) {
        if (success) viewModel.navigateToEnterNewPinScreen()
    }

    private fun openForgotPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateForResult(destinationId, 100)
        }
    }

    private fun openChangeCardPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(
                destinationId = destinationId, popupTo = R.id.currentCardPinFragment,
                extras = bundleOf(
                    CHANGE_PIN_MODEL.name to ChangeCardPinExtras(
                        screenType = SCREEN_TYPE_CHANGE_PIN,
                        oldCardPin = viewModel.viewState.pin.value
                    ), SCREEN_TYPE_KEY.name to SCREEN_TYPE_CHANGE_PIN.name
                )
            )
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.resultCode == 100) {
            navigateBack()
        } else Unit
    }

    override fun viewModelObservers() {
        observe(viewModel.verifyCardPinSuccess, ::navigateNext)
        observeEvent(viewModel.openForgotPin, ::openForgotPinScreen)
        observeEvent(viewModel.openChangeCardPin, ::openChangeCardPinScreen)
    }
}
