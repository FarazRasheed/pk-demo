package com.yap.yappk.pk.ui.dashboard.cards.cardstatus

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardStatusState @Inject constructor() : BaseState(), ICardStatus.State {
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
}