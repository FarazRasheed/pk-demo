package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarysearch

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter

interface IBankBeneficiarySearch {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun initSearchViewListener()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val bankBeneficiariesList: LiveData<List<IBeneficiary>>
        var beneficiaryListAdapter: BankBeneficiaryListAdapter?
        val resourcesProviders: ResourcesProviders
        fun setBeneficiaryList(beneficiaries: List<IBeneficiary>)
    }

    interface State : IBase.State
}