package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel

interface IBanksList {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val banksAdapter: BanksListAdapter
        val bankDataSuccess: LiveData<MutableList<BankData>>
        val errorDescription: LiveData<CharSequence>
        val openFetchAccountTitle: LiveData<SingleEvent<BankData>>
        fun banksListRequest()
        fun handleState(): ArrayList<AddBeneficiaryStateModel>
        fun navigateToFetchAccountScreen(bankData: BankData?)
    }

    interface State : IBase.State
}