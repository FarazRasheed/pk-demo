package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.banksearch

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist.BanksListAdapter

interface IBankSearch {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun initSearchViewListener()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val banksList: LiveData<List<BankData>>
        var banksListAdapter: BanksListAdapter?
        val resourcesProvider: ResourcesProviders
        fun setBanksList(banks: List<BankData>)
    }

    interface State : IBase.State
}