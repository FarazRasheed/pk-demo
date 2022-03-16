package com.yap.yappk.pk.ui.dashboard.cards.reorder.newcard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class ReorderNewCardState @Inject constructor() : BaseState(), IReorderNewCard.State {
    override var availableBalance: MutableLiveData<String> = MutableLiveData("PKR 0.00")
    override var reorderNewCardFee: MutableLiveData<String> = MutableLiveData("PKR 0.00")
    override var cardAddressTitle: MutableLiveData<String>? = MutableLiveData()
    override var cardAddressSubTitle: MutableLiveData<String>? = MutableLiveData()
}