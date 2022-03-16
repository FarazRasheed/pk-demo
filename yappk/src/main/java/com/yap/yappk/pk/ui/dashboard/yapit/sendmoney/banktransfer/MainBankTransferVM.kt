package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.BankBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.Beneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainBankTransferVM @Inject constructor(
    override val viewState: MainBankTransferState,
    private val customersApi: CustomersApi,
    override val resourcesProvider: ResourcesProviders
) : BaseViewModel<IMainBankTransfer.State>(), IMainBankTransfer.ViewModel {

    private val _recentBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val recentBeneficiariesList: LiveData<List<IBeneficiary>> = _recentBeneficiariesList

    private val _bankBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val bankBeneficiariesList: LiveData<List<IBeneficiary>> = _bankBeneficiariesList

    private val _isBeneficiaryDeleted = MutableLiveData<Boolean>()
    override val isBeneficiaryDeleted: LiveData<Boolean> get() = _isBeneficiaryDeleted

    override var recentTransferAdapter: CoreRecentTransferAdapter = CoreRecentTransferAdapter(
        mutableListOf()
    )
    override var bankBeneficiaryAdapter: BankBeneficiaryListAdapter? = null

    override fun getBeneficiaries() {
        showLoading()
        launch {
            val responseRecent = launchAsync { customersApi.getRecentTransfers("") }
            val responseBank = launchAsync { customersApi.getIBFTBeneficiaries() }
            withContext(Dispatchers.Main) {
                setResult(responseRecent.await(), responseBank.await())
            }
        }
    }

    private fun setResult(
        recentResponse: ApiResponse<BaseListResponse<Beneficiary>>,
        bankResponse: ApiResponse<BaseListResponse<BankBeneficiary>>
    ) {
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
        hideLoading()
    }

    override fun removeBeneficiary(beneficiaryId: String) {
        launch {
            showLoading(true)
            val response = customersApi.deleteIBFTBeneficiary(beneficiaryId)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _isBeneficiaryDeleted.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isBeneficiaryDeleted.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }
}