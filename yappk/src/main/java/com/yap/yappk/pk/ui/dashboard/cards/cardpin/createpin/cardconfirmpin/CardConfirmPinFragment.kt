package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.clickableSpan
import com.digitify.core.extensions.getColors
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.dialerpad.KeyClickListener
import com.yap.uikit.widget.dialerpad.KeyEvent
import com.yap.uikit.widget.dialerpad.OnSecureCodeListener
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardConfirmPinBinding
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter.CardPinEnterFragment
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.main.CardPinVM
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.main.ICardPin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardConfirmPinFragment :
    BaseNavFragment<FragmentCardConfirmPinBinding, ICardConfirmPin.State, ICardConfirmPin.ViewModel>(
        R.layout.fragment_card_confirm_pin
    ),
    ICardConfirmPin.View, ToolBarView.OnToolBarViewClickListener, KeyClickListener,
    OnSecureCodeListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardConfirmPin.ViewModel by viewModels<CardConfirmPinVM>()
    private val parentViewModel: ICardPin.ViewModel by activityViewModels<CardPinVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        getFragmentArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initPinCodeView()
        initTermAndCondition()
        setValues()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnPin -> {
                viewModel.handlePressOnCreate()
            }
        }
    }

    private fun setValues() {
        if (!viewModel.viewState.newPin.value.isNullOrBlank()) {
            viewModel.viewState.newPin.value?.forEach {
                mViewBinding.tvPassCode.input(it.toString())
                mViewBinding.keyBoard.input(it.toString())
            }
        }
    }

    private fun handlePinError(errorMsg: String) {
        if (errorMsg.isBlank()) {
            viewModel.setCardPin(
                viewModel.viewState.newPin.value ?: "",
                parentViewModel.debitCard?.cardSerialNumber ?: ""
            )
        }
    }

    private fun initTermAndCondition() {
        mViewBinding.tvTermsCondition.clickableSpan(
            Pair(
                "Terms & Conditions",
                View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putString("PAGE_URL", "https://www.yap.com/terms")
                    navigate(R.id.action_cardConfirmPinFragment_to_webViewFragment2, bundle)
                }), color = requireContext().getColors(R.color.pkBlueWithAHintOfPurple)
        )
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
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
        viewModel.viewState.newPin.value = code
        viewModel.viewState.valid.value = isCompleted
    }

    override fun onCodeChange(code: String?) {
        super.onCodeChange(code)
        viewModel.viewState.pinError.value = null
        viewModel.viewState.newPin.value = code
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.confirmPin = it.getString(CardPinEnterFragment::class.java.name) ?: ""
        }
    }

    private fun openCardPinSuccessScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId)
        }
    }

    private fun handleCardPinSuccess(isCardPinSaved: Boolean) {
        if (isCardPinSaved) {
            viewModel.openCardPinSuccessScreen()
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.viewState.pinError, ::handlePinError)
        observe(viewModel.isPinCodeSet, ::handleCardPinSuccess)
        observeEvent(viewModel.openCardPinSuccess, ::openCardPinSuccessScreen)
    }
}
