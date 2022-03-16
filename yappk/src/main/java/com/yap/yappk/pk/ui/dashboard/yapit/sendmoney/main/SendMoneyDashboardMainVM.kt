package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.Beneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SendMoneyDashboardMainVM @Inject constructor(
    override val viewState: SendMoneyDashboardMainState,
    private val customersApi: CustomersApi
) : BaseViewModel<ISendMoneyDashboardMain.State>(), ISendMoneyDashboardMain.ViewModel {
    private val _recentBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val recentBeneficiariesList: LiveData<List<IBeneficiary>> = _recentBeneficiariesList

    private val _yapContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val yapContacts: LiveData<List<Contact>> = _yapContacts

    private val _phoneContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val phoneContacts: LiveData<List<Contact>> = _phoneContacts


    override fun setYapContact(yapContact: List<Contact>) {
        _yapContacts.value = yapContact
    }

    override fun setPhoneContact(phoneContact: List<Contact>) {
        _phoneContacts.value = phoneContact
    }



    override fun getRecentTransferBeneficiaries() {
        launch {
            showLoading(true)
            val response = customersApi.getRecentTransfers("")
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _recentBeneficiariesList.value = response.data.data as List<Beneficiary>
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

}