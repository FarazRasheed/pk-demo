package com.yap.yappk.pk.ui.auth.forgotpasscode.createnewpasscode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.biometric.BiometricUtils
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
import com.yap.yappk.databinding.FragmentCreateNewPasscodeBinding
import com.yap.yappk.networking.microservices.customers.requestsdtos.CreateNewPasscodeRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewPasscodeFragment :
    BaseNavFragment<FragmentCreateNewPasscodeBinding, ICreateNewPasscode.State, ICreateNewPasscode.ViewModel>(
        R.layout.fragment_create_new_passcode
    ), ICreateNewPasscode.View, ToolBarView.OnToolBarViewClickListener, KeyClickListener,
    OnSecureCodeListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ICreateNewPasscode.ViewModel by viewModels<CreateNewPasscodeVM>()

    @Inject
    lateinit var biometricUtils: BiometricUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        initTermAndCondition()
        initPasscodeView()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnPasscode -> viewModel.handlePressOnCreatePasscode()
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun viewModelObservers() {
        observe(viewModel.isPasscodeCreated, ::handleNewPasscodeCreation)
        observe(viewModel.viewState.passcodeError, ::handlePasscodeError)
        observeEvent(viewModel.openCreateNewPasscodeSuccess, ::openCreateNewPasscodeSuccessScreen)
    }

    private fun initTermAndCondition() {
        mViewBinding.tvTermsCondition.clickableSpan(
            Pair(
                "Terms & Conditions",
                View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putString("PAGE_URL", "https://www.yap.com/terms")
                    navigate(R.id.action_createNewPasscodeFragment_to_webViewFragment2, bundle)
                }), color = requireContext().getColors(R.color.pkBlueWithAHintOfPurple)
        )
    }

    private fun handlePasscodeError(errorMsg: String) {
        if (errorMsg.isBlank()) {
            val request = CreateNewPasscodeRequest(
                newPassword = viewModel.viewState.passcode.value,
                token = viewModel.optToken,
                mobileNo = viewModel.mobileNumber
            )
            viewModel.createNewPasscode(request)
        }
    }

    private fun handleNewPasscodeCreation(isNewPasscodeCreated: Boolean) {
        if (isNewPasscodeCreated) {
            savePassCode()
            viewModel.openCreateNewPasscodeSuccessScreen()
        }
    }

    private fun savePassCode() {
        val has = biometricUtils.hasBioMetricFeature(requireActivity())
        if (viewModel.isBiometricLoginEnabled(has))
            viewModel.savePassCode()
    }

    private fun openCreateNewPasscodeSuccessScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(destinationId, R.id.createNewPasscodeFragment)
        }
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.mobileNumber = it.getString("mobileNumber") ?: ""
            viewModel.optToken = it.getString("otpToken")
        }
    }

    private fun initPasscodeView() {
        mViewBinding.keyBoard.setKeyClickListener(this)
        mViewBinding.keyBoard.setOnSecureCodeListener(this)
    }

    override fun onKeyClicked(view: View, which: KeyEvent) {
        when (which) {
            KeyEvent.LEFT -> {
                mViewBinding.keyBoard.delete()
                viewModel.viewState.passcodeError.value = null
            }
            else -> {
                mViewBinding.keyBoard.input(which.value)
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

}
