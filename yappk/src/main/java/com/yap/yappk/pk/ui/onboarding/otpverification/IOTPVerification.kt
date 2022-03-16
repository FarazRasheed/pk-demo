package com.yap.yappk.pk.ui.onboarding.otpverification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.requestdtos.VerifyOtpOnboardingRequest

interface IOTPVerification {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var otp: String
        val otpEvent: LiveData<Boolean>
        var optToken: String?
        val resetOTPEvent: MutableLiveData<Boolean>
        var mobileNumber: String
        var countryCode: String
        val openCreatePassCode: LiveData<SingleEvent<Int>>

        fun verifyOtp(otpRequest: VerifyOtpOnboardingRequest)
        fun reCreateOtp(otpCreationRequest: CreateOtpOnboardingRequest)
        fun resetOtp()
        fun getVerifyOtpRequest(): VerifyOtpOnboardingRequest
        fun getCreateOtpOnBoardingRequest(): CreateOtpOnboardingRequest
        fun openCreatePassCodeScreen()
    }

    interface State : IBase.State {
        var isValid: MutableLiveData<Boolean>
        var timer: MutableLiveData<String>
        var validResend: MutableLiveData<Boolean>
        var formattedNumber: MutableLiveData<String>
        var color: MutableLiveData<Int>
    }
}