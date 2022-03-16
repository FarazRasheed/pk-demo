package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class TopUpViaExternalCardState @Inject constructor() : BaseState(), ITopUpViaExternalCard.State {
    override var transferFee: MutableLiveData<String> = MutableLiveData("PKR 0.00")
    override var minLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var maxLimit: MutableLiveData<Double>? = MutableLiveData(0.0)
    override var accountCurrentBalance: MutableLiveData<String>? = MutableLiveData("PKR 0.00")
    override var isFocusable: MutableLiveData<Boolean> = MutableLiveData()
    override var isValidAmount: MutableLiveData<Boolean> = MutableLiveData()
}