package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardConfirmPinState @Inject constructor() : BaseState(), ICardConfirmPin.State {
    override var newPin: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var pinError: MutableLiveData<String> = MutableLiveData()
}