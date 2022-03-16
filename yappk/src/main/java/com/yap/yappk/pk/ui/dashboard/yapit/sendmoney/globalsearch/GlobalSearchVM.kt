package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.BankBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.Beneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch.adapter.BeneficiaryListAdapter
import com.yap.yappk.pk.utils.PhoneContactsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GlobalSearchVM @Inject constructor(
    override val viewState: GlobalSearchState,
    private val phoneContactsProvider: PhoneContactsProvider,
    private val customersApi: CustomersApi,
    override val resourcesProviders: ResourcesProviders
) : BaseViewModel<IGlobalSearch.State>(), IGlobalSearch.ViewModel {

    private val _localContacts: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val localContacts: LiveData<List<IBeneficiary>> = _localContacts

    private val _yapContacts: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val yapContacts: LiveData<List<IBeneficiary>> = _yapContacts

    private val _recentBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val recentBeneficiariesList: LiveData<List<IBeneficiary>> = _recentBeneficiariesList

    private val _bankBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val bankBeneficiariesList: LiveData<List<IBeneficiary>> = _bankBeneficiariesList

    private val _beneficiariesList: MutableLiveData<List<IBeneficiary>> =
        MutableLiveData()
    override val beneficiariesList: LiveData<List<IBeneficiary>> = _beneficiariesList

    override var beneficiaryListAdapter: BeneficiaryListAdapter? = null

    override fun getLocalContacts() {
        launch(Dispatcher.LongOperation) {
            showLoading(onBackGround = true)
            val contacts = phoneContactsProvider.getLocalContacts()
            withContext(Dispatchers.Main) {
                _localContacts.value = contacts
            }
        }
    }

    override fun getBeneficiariesFrom(contacts: List<Contact>) {
        launch {
            val responseY2Y = launchAsync { customersApi.getY2YUsers(contacts) }
            val responseRecent = launchAsync { customersApi.getRecentTransfers("") }
            val responseBank = launchAsync { customersApi.getIBFTBeneficiaries()}

            withContext(Dispatchers.Main) {
                setResult(responseY2Y.await(), responseRecent.await(), responseBank.await())
            }
        }
    }

    private fun setResult(
        y2yContactResponse: ApiResponse<BaseListResponse<Contact>>,
        recentResponse: ApiResponse<BaseListResponse<Beneficiary>>,
        bankResponse: ApiResponse<BaseListResponse<BankBeneficiary>>
    ) {
        when (y2yContactResponse) {
            is ApiResponse.Success -> {
                _yapContacts.value =
                    y2yContactResponse.data.data?.filter { yapContact -> yapContact.yapUser == true }
            }
            is ApiResponse.Error -> {
                showAlertMessage(y2yContactResponse.error.message)
                _yapContacts.value = null

            }
        }
        when (recentResponse) {
            is ApiResponse.Success -> {
                _recentBeneficiariesList.value =
                    recentResponse.data.data
            }
            is ApiResponse.Error -> {
                showAlertMessage(recentResponse.error.message)
                _recentBeneficiariesList.value = null

            }
        }

        when (bankResponse) {
            is ApiResponse.Success -> {
                _bankBeneficiariesList.value =
                    bankResponse.data.data
            }
            is ApiResponse.Error -> {
                showAlertMessage(bankResponse.error.message)
                _bankBeneficiariesList.value = null

            }
        }
        val beneficiaries: ArrayList<IBeneficiary> = arrayListOf()
        beneficiaries.addAll(_yapContacts.value ?: arrayListOf())
        beneficiaries.addAll(_recentBeneficiariesList.value ?: arrayListOf())
        beneficiaries.addAll(_bankBeneficiariesList.value ?: arrayListOf())
        _beneficiariesList.value = beneficiaries.distinctBy {
            it.accountUUID
        }.sortedBy { it.fullName }
        hideLoading()
    }
}