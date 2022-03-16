package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountdetails

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class AccountDetailState @Inject constructor() : BaseState(), IAccountDetail.State {
    override val accountTitle: MutableLiveData<String> = MutableLiveData()
    override val iban: MutableLiveData<String> = MutableLiveData()
    override val accountNumber: MutableLiveData<String> = MutableLiveData()
}
