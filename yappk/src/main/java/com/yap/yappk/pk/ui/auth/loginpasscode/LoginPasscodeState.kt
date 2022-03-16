package com.yap.yappk.pk.ui.auth.loginpasscode

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class LoginPasscodeState @Inject constructor() : BaseState(), ILoginPassCode.State {
    override var deviceId: String = ""
    override var passcode: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var passcodeError: MutableLiveData<String> = MutableLiveData()
    override var biometricEnable: MutableLiveData<Boolean> = MutableLiveData(false)
    override val isScreenLocked: MutableLiveData<Boolean> = MutableLiveData()
    override val isAccountLocked: MutableLiveData<Boolean> = MutableLiveData()

}
