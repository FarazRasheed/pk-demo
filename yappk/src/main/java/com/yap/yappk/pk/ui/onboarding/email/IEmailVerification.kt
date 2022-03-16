package com.yap.yappk.pk.ui.onboarding.email

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.EmailVerificationRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.SignUpRequest
import com.yap.yappk.pk.SessionManager

interface IEmailVerification {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val savProfileEvent: LiveData<Boolean>
        val animationStartEvent: LiveData<Boolean>
        val showError: LiveData<SingleEvent<String?>>
        val sessionManager: SessionManager
        val sharedPreferenceManager: SharedPreferenceManager
        val openSignupSuccess: LiveData<SingleEvent<Bundle>>
        fun openSignupSuccessScreen()

        fun verifyEmailAndSignup(
            emailVerificationRequest: EmailVerificationRequest,
            signUpRequest: SignUpRequest,
            postDataRequest: DemographicDataRequest
        )
    }

    interface State : IBase.State {
        var email: MutableLiveData<String>
        var isValid: MutableLiveData<Boolean>
    }
}