package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.dashboard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class SendMoneyDashboardState @Inject constructor() : BaseState(), ISendMoneyDashboard.State {
    override var isNoRecentTransfers: MutableLiveData<Boolean> = MutableLiveData()
    override var isRecentTransfersVisible: MutableLiveData<Boolean> = MutableLiveData()
}