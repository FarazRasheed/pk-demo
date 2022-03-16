package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.newcardpin

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import com.yap.yappk.pk.ui.dashboard.cards.models.ChangeCardPinExtras
import javax.inject.Inject

class ChangeCardPinState @Inject constructor() : BaseState(), IChangeCardPin.State {
    override var screenType: MutableLiveData<String> = MutableLiveData()
    override var changeCardPinModel: MutableLiveData<ChangeCardPinExtras> = MutableLiveData()
    override var pin: MutableLiveData<String> = MutableLiveData()
    override var pinError: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData(false)
}