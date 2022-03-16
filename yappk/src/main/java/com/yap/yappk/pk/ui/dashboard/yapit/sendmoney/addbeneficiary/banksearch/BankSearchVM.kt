package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.banksearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist.BanksListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankSearchVM @Inject constructor(
    override val viewState: BankSearchState,
    override val resourcesProvider: ResourcesProviders
) :
    BaseViewModel<IBankSearch.State>(), IBankSearch.ViewModel {
    private val _banksList: MutableLiveData<List<BankData>> = MutableLiveData()
    override val banksList: LiveData<List<BankData>> = _banksList
    override var banksListAdapter: BanksListAdapter? =
        BanksListAdapter(banksList = mutableListOf(), resourcesProviders = resourcesProvider)

    override fun setBanksList(banks: List<BankData>) {
        _banksList.value = banks
    }
}
