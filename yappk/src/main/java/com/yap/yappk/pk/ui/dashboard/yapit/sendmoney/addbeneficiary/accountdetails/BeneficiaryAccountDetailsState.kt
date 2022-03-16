package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class BeneficiaryAccountDetailsState @Inject constructor() : BaseState(), IBeneficiaryAccountDetails.State {
    override var isValidaNickName: MutableLiveData<Boolean> = MutableLiveData()
}