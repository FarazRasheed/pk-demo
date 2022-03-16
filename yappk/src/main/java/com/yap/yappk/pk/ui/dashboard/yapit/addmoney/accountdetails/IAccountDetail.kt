package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountdetails

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.SessionManager

interface IAccountDetail {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>{
        val sessionManager: SessionManager
        fun getShareBody(): String
    }
    interface State : IBase.State{
        val accountTitle: MutableLiveData<String>
        val iban: MutableLiveData<String>
        val accountNumber: MutableLiveData<String>

    }
}