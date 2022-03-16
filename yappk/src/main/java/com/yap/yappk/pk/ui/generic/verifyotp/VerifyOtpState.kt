package com.yap.yappk.pk.ui.generic.verifyotp

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class VerifyOtpState @Inject constructor() : BaseState(),
    IVerifyOtp.State {
    override var isValid: MutableLiveData<Boolean> = MutableLiveData()
    override var timer: MutableLiveData<String> = MutableLiveData()
    override var validResend: MutableLiveData<Boolean> = MutableLiveData()
    override var formattedNumber: MutableLiveData<String> = MutableLiveData()
    override var color: MutableLiveData<Int> = MutableLiveData()
}