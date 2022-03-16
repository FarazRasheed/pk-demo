package com.yap.yappk.pk.ui.onboarding.otpverification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.otptextview.OTPListener
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentOtpVerificationBinding
import com.yap.yappk.pk.ui.onboarding.main.IMain
import com.yap.yappk.pk.ui.onboarding.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPVerificationFragment :
    BaseNavFragment<FragmentOtpVerificationBinding, IOTPVerification.State, IOTPVerification.ViewModel>(
        R.layout.fragment_otp_verification
    ), IOTPVerification.View, OTPListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IOTPVerification.ViewModel by viewModels<OTPVerificationVM>()
    private val parentViewModel: IMain.ViewModel by activityViewModels<MainViewModel>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnVerifyOtp -> {
                viewModel.verifyOtp(viewModel.getVerifyOtpRequest())
            }
            R.id.btnResend -> {
                viewModel.reCreateOtp(viewModel.getCreateOtpOnBoardingRequest())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRouteEventsObservers()
        setObserver()
    }

    private fun setObserver() {
        observe(viewModel.otpEvent, ::moveDataOnOtpVerification)
    }

    private fun moveDataOnOtpVerification(isOtpVerified: Boolean?) {
        if (isOtpVerified == true) {
            parentViewModel.signupData.token = viewModel.optToken
            viewModel.openCreatePassCodeScreen()
        } else {
            mViewBinding.otpView.setOTP("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        mViewBinding.otpView.otpListener = this
    }

    private fun openCreatePassCodeScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(destinationId, R.id.otpVerificationFragment)
        }
    }

    private fun setRouteEventsObservers() {
        observeEvent(viewModel.openCreatePassCode, ::openCreatePassCodeScreen)
    }

    private fun init() {

        viewModel.countryCode = parentViewModel.signupData.countryCode ?: ""
        viewModel.mobileNumber = parentViewModel.signupData.mobileNo ?: ""
        viewModel.viewState.formattedNumber.value = getFormattedPhoneNumber(
            requireContext(),
            viewModel.countryCode.replace("+", "00") + viewModel.mobileNumber
        )
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.setProgressVisibility(true)
        parentViewModel.setProgress(40)
        mViewBinding.otpView.requestFocusOTP()
    }

    override fun onInteractionListener() {
        viewModel.viewState.isValid.value = false
    }

    override fun onOTPComplete(otp: String) {
        viewModel.otp = otp
        viewModel.viewState.isValid.value = true
        viewModel.verifyOtp(viewModel.getVerifyOtpRequest())
    }

}