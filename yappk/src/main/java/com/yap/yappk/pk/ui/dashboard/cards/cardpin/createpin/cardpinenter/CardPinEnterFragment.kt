package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter

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
import com.yap.yappk.databinding.FragmentCardPinEnterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardPinEnterFragment :
    BaseNavFragment<FragmentCardPinEnterBinding, ICardPinEnter.State, ICardPinEnter.ViewModel>(R.layout.fragment_card_pin_enter),
    ICardPinEnter.View, ToolBarView.OnToolBarViewClickListener, KeyClickListener,
    OnSecureCodeListener {
    override fun getBindingVariable(): Int = BR.viewModel
    val viewModels = viewModels<CardPinEnterVM>()
    override val viewModel: ICardPinEnter.ViewModel by viewModels
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initPinCodeView()
        setValues()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                viewModel.handlePressOnNext()
            }
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    private fun setValues() {
        if (!viewModel.viewState.pin.value.isNullOrBlank()) {
            viewModel.viewState.pin.value?.forEach {
                mViewBinding.tvPassCode.input(it.toString())
                mViewBinding.keyBoard.input(it.toString())
            }
        }
    }

    private fun initPinCodeView() {
        mViewBinding.keyBoard.setKeyClickListener(this)
        mViewBinding.keyBoard.setOnSecureCodeListener(this)
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
            viewModel.openCardConfirmPinScreen()
        }
    }

    private fun openCardConfirmPinScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            val bundle = Bundle()
            bundle.putString(CardPinEnterFragment::class.java.name, viewModel.viewState.pin.value)
            navigate(destinationId, bundle)
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.viewState.pinError, ::handlePinError)
        observeEvent(viewModel.openConfirmPin, ::openCardConfirmPinScreen)
    }

}
