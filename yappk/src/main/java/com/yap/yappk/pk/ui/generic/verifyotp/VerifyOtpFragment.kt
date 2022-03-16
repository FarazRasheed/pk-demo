package com.yap.yappk.pk.ui.generic.verifyotp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.utils.MySMSBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.otptextview.OTPListener
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentVerifyOtpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment :
    BaseNavFragment<FragmentVerifyOtpBinding, IVerifyOtp.State, IVerifyOtp.ViewModel>(
        R.layout.fragment_verify_otp
    ),
    IVerifyOtp.View, ToolBarView.OnToolBarViewClickListener,
    MySMSBroadcastReceiver.OnSmsReceiveListener,
    OTPListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IVerifyOtp.ViewModel by viewModels<VerifyOtpVM>()

    private var intentFilter: IntentFilter? = null
    private var appSMSBroadcastReceiver: MySMSBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.otpView.otpListener = this
        viewModel.viewState.formattedNumber.value = getFormattedPhoneNumber(
            requireContext(),
            viewModel.sessionManager.userAccount.value?.currentCustomer?.countryCode?.replace(
                "+",
                "00"
            ) + viewModel.sessionManager.userAccount.value?.currentCustomer?.mobileNo
        )
        getFragmentArguments()
        initBroadCastForSMS()
        setValues()
    }

    private fun setValues() {
        mViewBinding.verifyTitleTV.text = viewModel.getTitle(viewModel.action)
        mViewBinding.tbView.tbTitleText = viewModel.getTbTitle(viewModel.action)
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.action =
                it.getString("otp_action") ?: ""
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnResend -> viewModel.createOtp(viewModel.action)
            R.id.btnVerifyOtp -> onOTPVerifyClick()
        }
    }

    private fun initBroadCastForSMS() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        appSMSBroadcastReceiver = MySMSBroadcastReceiver(this)
    }

    override fun onOTPComplete(otp: String) {
        viewModel.otp = otp
        viewModel.viewState.isValid.value = true
        onOTPVerifyClick()
    }

    override fun onOTPVerifyClick() {
        viewModel.verifyOtp(
            action = viewModel.action,
            otp = viewModel.otp
        )
    }

    override fun onReceive(code: Intent?) {
        code?.let {
            it.resolveActivity(requireContext().packageManager)?.run {
            }
        }
    }

    override fun onInteractionListener() {
        viewModel.viewState.isValid.value = false
    }

    private fun handleForgotCardPinCreated(isOtpCreated: Boolean) {
        if (isOtpCreated) {
            viewModel.reverseTimer(10)
        }
    }

    private fun handleForgotCardPinVerified(isOtpVerified: Boolean) {
        if (isOtpVerified) {
            navigateBackWithResult(200, bundleOf("otp_token" to viewModel.token))
        } else {
            mViewBinding.otpView.setOTP("")
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.isForgotCardPinOtpCreated, ::handleForgotCardPinCreated)
        observe(viewModel.isForgotCardPinOtpVerified, ::handleForgotCardPinVerified)
    }
}
