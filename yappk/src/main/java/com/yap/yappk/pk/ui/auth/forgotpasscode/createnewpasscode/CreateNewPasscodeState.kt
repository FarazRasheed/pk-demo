package com.yap.yappk.pk.ui.auth.forgotpasscode.createnewpasscode

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CreateNewPasscodeState @Inject constructor() : BaseState(), ICreateNewPasscode.State {
    override var passcode: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var passcodeError: MutableLiveData<String> = MutableLiveData()
}
