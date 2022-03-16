package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class PrimaryCardDetailDashboardState @Inject constructor() : BaseState(),
    IPrimaryCardDetailDashboard.State {
    override val cardName: MutableLiveData<String> = MutableLiveData()
    override val cardType: MutableLiveData<String> = MutableLiveData()
    override val cardMaskNo: MutableLiveData<String> = MutableLiveData()
    override val cardBalance: MutableLiveData<String> = MutableLiveData()
    override val cardFreezeText: MutableLiveData<String> = MutableLiveData()
    override val isCardFreezes: MutableLiveData<Boolean> = MutableLiveData()
}