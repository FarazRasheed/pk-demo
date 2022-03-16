package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class BankTransferState @Inject constructor() : BaseState(), IBankTransfer.State {
    override var isValidAmount: MutableLiveData<Boolean> = MutableLiveData(false)
    override var isFocusable: MutableLiveData<Boolean> = MutableLiveData(false)
    override var transferFee: MutableLiveData<String> = MutableLiveData("0.00")
    override var minLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var maxLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var accountCurrentBalance: MutableLiveData<String>? = MutableLiveData("0.00")
}