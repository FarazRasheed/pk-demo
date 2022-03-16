package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class MainBankTransferState @Inject constructor() : BaseState(), IMainBankTransfer.State {
    override val isNoRecentTransfers: MutableLiveData<Boolean> = MutableLiveData()
    override val isBeneficiariesAvailable: MutableLiveData<Boolean> = MutableLiveData()
    override val isRecentTransfersVisible: MutableLiveData<Boolean> = MutableLiveData()
}