package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardDashboardState @Inject constructor() : BaseState(), ICardDashboard.State {
    override val tbTitle: MutableLiveData<String> = MutableLiveData()
}