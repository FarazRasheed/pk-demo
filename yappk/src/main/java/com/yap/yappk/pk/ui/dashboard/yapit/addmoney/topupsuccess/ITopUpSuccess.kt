package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.topupsuccess

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager

interface ITopUpSuccess {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val card: LiveData<ExternalCard?>
        val sessionManager: SessionManager
        fun setCard(card: ExternalCard?)
        fun setTopUpAmount(amount: String?)
        val topUpAmount: LiveData<String?>
        val availableBalance: LiveData<String?>
        fun getAccountBalance()
    }

    interface State : IBase.State
}