package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.cardcvv

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardCvvState @Inject constructor() : BaseState(), ICardCvv.State {
    override var cvv: MutableLiveData<String> = MutableLiveData()
}