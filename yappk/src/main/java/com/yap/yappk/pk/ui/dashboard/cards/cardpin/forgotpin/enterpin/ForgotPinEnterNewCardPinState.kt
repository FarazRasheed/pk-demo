package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.enterpin

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class ForgotPinEnterNewCardPinState @Inject constructor() : BaseState(),
    IForgotPinEnterNewCardPin.State {
    override var pin: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var isConfirmedPin: MutableLiveData<Boolean> = MutableLiveData()
    override var pinError: MutableLiveData<String> = MutableLiveData()
}