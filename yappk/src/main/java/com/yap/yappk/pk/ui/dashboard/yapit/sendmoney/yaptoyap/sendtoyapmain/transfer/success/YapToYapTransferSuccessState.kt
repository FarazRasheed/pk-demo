package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.success

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.transactions.responsedtos.YapToYapTransaction
import javax.inject.Inject

class YapToYapTransferSuccessState @Inject constructor() : BaseState(), IYapToYapTransferSuccess.State {
    override var contact: MutableLiveData<IBeneficiary?> = MutableLiveData()
    override var yapToYapTransaction: MutableLiveData<YapToYapTransaction> = MutableLiveData()
    override var transferredAmount: MutableLiveData<String> = MutableLiveData()
}