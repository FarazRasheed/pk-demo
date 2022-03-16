package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionfilter

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardTransactionFilterState @Inject constructor() : BaseState(),
    ICardTransactionFilter.State {
    override val isAtmWithdrawAllow: MutableLiveData<Boolean> = MutableLiveData(true)
    override val isRetailPayment: MutableLiveData<Boolean> = MutableLiveData(true)
    override val minRange: MutableLiveData<String> = MutableLiveData()
    override val maxRange: MutableLiveData<String> = MutableLiveData()

}