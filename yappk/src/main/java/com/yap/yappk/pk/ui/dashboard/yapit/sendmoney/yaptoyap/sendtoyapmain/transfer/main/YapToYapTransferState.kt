package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import javax.inject.Inject

class YapToYapTransferState @Inject constructor() : BaseState(), IYapToYapTransfer.State {
    override var contact: MutableLiveData<IBeneficiary> = MutableLiveData()
    override var isValidAmount: MutableLiveData<Boolean> = MutableLiveData(false)
    override var isFocusable: MutableLiveData<Boolean> = MutableLiveData(false)
    override var transferFee: MutableLiveData<String> = MutableLiveData("PKR 0.00")
    override var minLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var maxLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var accountCurrentBalance: MutableLiveData<String>? = MutableLiveData("PKR 0.00")
}