package com.yap.yappk.pk.ui.dashboard.cards

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardsState @Inject constructor() : BaseState(), ICards.State {
    override val isListView: MutableLiveData<Boolean> = MutableLiveData(false)
    override var isAccountCreated: MutableLiveData<Boolean> = MutableLiveData(false)
}