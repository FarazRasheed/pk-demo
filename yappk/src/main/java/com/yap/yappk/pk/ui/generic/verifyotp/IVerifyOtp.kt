package com.yap.yappk.pk.ui.generic.verifyotp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.SessionManager

interface IVerifyOtp {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
        fun onOTPVerifyClick()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var action: String
        var otp: String
        var token: String?
        val sessionManager: SessionManager
        val isForgotCardPinOtpCreated: LiveData<Boolean>
        val isForgotCardPinOtpVerified: LiveData<Boolean>
        fun createOtp(action: String)
        fun reverseTimer(totalSeconds: Long)
        fun verifyOtp(otp: String, action: String)
        fun getTitle(action: String): String
        fun getTbTitle(action: String): String?
    }

    interface State : IBase.State {
        var isValid: MutableLiveData<Boolean>
        var timer: MutableLiveData<String>
        var validResend: MutableLiveData<Boolean>
        var formattedNumber: MutableLiveData<String>
        var color: MutableLiveData<Int>
    }
}