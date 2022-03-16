package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.verifypasscode

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class ForgotPinVerifyPasscodeState @Inject constructor() : BaseState(),
    IForgotPinVerifyPasscode.State {
    override var passcode: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var passcodeError: MutableLiveData<String> = MutableLiveData()
}