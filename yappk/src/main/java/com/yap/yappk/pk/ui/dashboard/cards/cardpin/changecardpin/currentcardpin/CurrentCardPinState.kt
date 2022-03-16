package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.currentcardpin

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CurrentCardPinState @Inject constructor() : BaseState(), ICurrentCardPin.State {
    override var pin: MutableLiveData<String> = MutableLiveData()
    override var pinError: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
}