package com.yap.yappk.pk.ui.auth.forgotpasscode.verifyforgototp

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class VerifyForgotOTPState @Inject constructor() : BaseState(), IVerifyForgotOTP.State {
    override var isValid: MutableLiveData<Boolean> = MutableLiveData(false)
    override var timer: MutableLiveData<String> = MutableLiveData()
    override var validResend: MutableLiveData<Boolean> = MutableLiveData()
    override var formattedNumber: MutableLiveData<String> = MutableLiveData()
    override var color: MutableLiveData<Int> = MutableLiveData()
}