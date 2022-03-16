package com.yap.yappk.pk.ui.onboarding.email

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class EmailVerificationState @Inject constructor() : BaseState(), IEmailVerification.State {
    override var email: MutableLiveData<String> = MutableLiveData()
    override var isValid: MutableLiveData<Boolean> = MutableLiveData()
}