package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class FindBeneficiaryAccountState @Inject constructor() : BaseState(), IFindBeneficiaryAccount.State {
    override var isValidaAccountHolderNumber: MutableLiveData<Boolean> = MutableLiveData()
}