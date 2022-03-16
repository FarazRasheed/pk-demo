package com.yap.yappk.pk.ui.onboarding.passcode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.clickableSpan
import com.digitify.core.extensions.getColors
import com.yap.uikit.widget.dialerpad.KeyClickListener
import com.yap.uikit.widget.dialerpad.KeyEvent
import com.yap.uikit.widget.dialerpad.OnSecureCodeListener
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentPasscodeBinding
import com.yap.yappk.pk.ui.onboarding.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasscodeFragment :
    BaseNavFragment<FragmentPasscodeBinding, IPassCode.State, IPassCode.ViewModel>(R.layout.fragment_passcode) {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: PasscodeVM by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTermAndCondition()
        initPasscodeView()
    }

    private fun addObservers() {
        viewModel.viewState.passcodeError.observe(this, {
            it?.let {
                if (it.isBlank()) {
                    parentViewModel.signupData.passcode = viewModel.viewState.passcode.value
                    navigateWithPopup(R.id.nameVerificationFragment, R.id.passcodeFragment)
                }
            }
        })
    }

    private fun initTermAndCondition() {
        mViewBinding.tvTermsCondition.clickableSpan(
            Pair(
                "Terms & Conditions",
                View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putString("PAGE_URL", "https://www.yap.com/terms")
                    navigate(R.id.action_passcodeFragment_to_webViewFragment, bundle)
                }), color = requireContext().getColors(R.color.pkBlueWithAHintOfPurple)
        )
    }

    private fun initPasscodeView() {
        mViewBinding.keyBoard.setKeyClickListener(object : KeyClickListener {
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
        })
        mViewBinding.keyBoard.setOnSecureCodeListener(object : OnSecureCodeListener {
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
        })
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivBack -> {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.setProgressVisibility(false)
    }

}