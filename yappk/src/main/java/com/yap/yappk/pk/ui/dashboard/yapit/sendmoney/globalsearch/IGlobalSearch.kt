package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch.adapter.BeneficiaryListAdapter

interface IGlobalSearch {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun initSearchViewListener()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val localContacts: LiveData<List<IBeneficiary>>
        val yapContacts: LiveData<List<IBeneficiary>>
        val recentBeneficiariesList: LiveData<List<IBeneficiary>>
        val beneficiariesList: LiveData<List<IBeneficiary>>
        val bankBeneficiariesList: LiveData<List<IBeneficiary>>
        var beneficiaryListAdapter: BeneficiaryListAdapter?
        val resourcesProviders: ResourcesProviders
        fun getLocalContacts()
        fun getBeneficiariesFrom(contacts: List<Contact>)
    }

    interface State : IBase.State
}