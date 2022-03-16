package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.networking.microservices.customers.responsedtos.FindBankAccountInfoResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FindBeneficiaryAccountVM @Inject constructor(
    override val viewState: FindBeneficiaryAccountState,
    private val customersApi: CustomersApi
) :
    BaseViewModel<IFindBeneficiaryAccount.State>(), IFindBeneficiaryAccount.ViewModel {
    private var _bankDetails: MutableLiveData<BankData> = MutableLiveData()
    override val bankDetails: LiveData<BankData> = _bankDetails
    private var _bankAccountData: MutableLiveData<FindBankAccountInfoResponse> = MutableLiveData()
    override val bankAccountData: LiveData<FindBankAccountInfoResponse> = _bankAccountData
    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

    override fun setBankDetails(bankData: BankData?) {
        _bankDetails.value = bankData
    }

    override fun onTextChange(number: CharSequence, start: Int, before: Int, count: Int) {
        viewState.isValidaAccountHolderNumber.value = when {
            number.length >= _bankDetails.value?.accountNoMinLength ?: 0 && number.length <= _bankDetails.value?.ibanMaxLength ?: 0 -> true
            else -> false
        }
    }

    override fun fetchAccountInfoRequest(accountNo: String?, iban: String?, consumerId: String?) {
        launch {
            showLoading(true)
            val response =
                customersApi.findAccountInfo(accountNo = accountNo, iban = iban, consumerId = consumerId)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        val bankResponse = response.data.data
                        bankResponse?.let { bankAccountInfo ->
                            _bankDetails.value.also {
                                it?.accountHolderTitle = bankAccountInfo.title
                                it?.accountNumber = bankAccountInfo.accountNo
                                it?.ibanNumber = bankAccountInfo.iban
                            }
                        }
                        _bankAccountData.value = bankResponse
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        _errorDescription.value = response.error.message
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun handleState(): ArrayList<AddBeneficiaryStateModel> {
        val list = ArrayList<AddBeneficiaryStateModel>()
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
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
        return list
    }
}