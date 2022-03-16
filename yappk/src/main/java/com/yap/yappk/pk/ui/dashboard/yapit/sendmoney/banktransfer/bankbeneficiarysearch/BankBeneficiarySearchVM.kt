package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarysearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankBeneficiarySearchVM @Inject constructor(
    override val viewState: BankBeneficiarySearchState,
    override val resourcesProviders: ResourcesProviders
) : BaseViewModel<IBankBeneficiarySearch.State>(), IBankBeneficiarySearch.ViewModel {

    private val _bankBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val bankBeneficiariesList: LiveData<List<IBeneficiary>> = _bankBeneficiariesList

    override var beneficiaryListAdapter: BankBeneficiaryListAdapter? = null

    override fun setBeneficiaryList(beneficiaries: List<IBeneficiary>) {
        _bankBeneficiariesList.value = beneficiaries
    }

}