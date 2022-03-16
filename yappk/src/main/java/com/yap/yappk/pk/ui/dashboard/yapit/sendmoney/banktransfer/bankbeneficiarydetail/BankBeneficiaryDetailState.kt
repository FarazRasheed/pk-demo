package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class BankBeneficiaryDetailState @Inject constructor() : BaseState(), IBankBeneficiaryDetail.State {
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
}