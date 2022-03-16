package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BanksListVM @Inject constructor(
    override val viewState: BanksListState,
    private val customersApi: CustomersApi
) :
    BaseViewModel<IBanksList.State>(), IBanksList.ViewModel {
    private val _banksListSuccess = MutableLiveData<MutableList<BankData>>()
    override val bankDataSuccess: LiveData<MutableList<BankData>> = _banksListSuccess

    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

    private val _openFetchAccountTitle = MutableLiveData<SingleEvent<BankData>>()
    override val openFetchAccountTitle: LiveData<SingleEvent<BankData>> = _openFetchAccountTitle

    override val banksAdapter: BanksListAdapter =
        BanksListAdapter(arrayListOf())

    override fun banksListRequest() {
        launch {
            showLoading(true)
            val response =
                customersApi.getBanksList()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _banksListSuccess.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _errorDescription.value = response.error.message
                    }
                }
            }
        }
    }

    override fun navigateToFetchAccountScreen(bankData: BankData?) {
        bankData?.let {
            _openFetchAccountTitle.postValue(SingleEvent(bankData))
        }
    }

    override fun handleState(): ArrayList<AddBeneficiaryStateModel> {
        val list = ArrayList<AddBeneficiaryStateModel>()
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.IN_PROGRESS.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.PENDING.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.PENDING.name
            )
        )
        return list
    }
}
