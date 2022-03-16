package com.yap.yappk.pk.ui.onboarding.fullname

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class NameVerificationState @Inject constructor() : BaseState(), INameVerification.State {
    override var firstName: MutableLiveData<String> = MutableLiveData("")
    override var lastName: MutableLiveData<String> = MutableLiveData("")
    override var isValid: MutableLiveData<Boolean> = MutableLiveData()
}