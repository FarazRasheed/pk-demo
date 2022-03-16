package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardPinEnterState @Inject constructor() : BaseState(), ICardPinEnter.State {
    override var pin: MutableLiveData<String> = MutableLiveData()
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
    override var pinError: MutableLiveData<String> = MutableLiveData()
}