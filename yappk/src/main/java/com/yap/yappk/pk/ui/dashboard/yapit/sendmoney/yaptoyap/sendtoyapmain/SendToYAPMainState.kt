package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class SendToYAPMainState @Inject constructor() : BaseState(), ISendToYAPMain.State{
    override val isNoRecentTransfers: MutableLiveData<Boolean> = MutableLiveData()
    override val isRecentTransfersVisible: MutableLiveData<Boolean> = MutableLiveData()
}