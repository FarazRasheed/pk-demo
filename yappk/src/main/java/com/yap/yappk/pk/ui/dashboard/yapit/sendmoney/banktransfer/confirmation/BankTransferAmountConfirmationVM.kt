package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.confirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.BankTransferResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BankTransferAmountConfirmationVM @Inject constructor(
    override val viewState: BankTransferAmountConfirmationState, val transactionsApi: TransactionsApi
) :
    BaseViewModel<IBankTransferAmountConfirmation.State>(), IBankTransferAmountConfirmation.ViewModel {
    private val _beneficiary: MutableLiveData<BankTransferDataModel> = MutableLiveData()
    override val beneficiary: LiveData<BankTransferDataModel> = _beneficiary
    private val _bankTransferResponse: MutableLiveData<BankTransferResponse?> = MutableLiveData()
    override val bankTransferResponse: LiveData<BankTransferResponse?> = _bankTransferResponse
    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

    override fun setBeneficiary(beneficiary: BankTransferDataModel?) {
        _beneficiary.value = beneficiary
    }

    override fun bankFundsTransferRequest(
        beneficiaryId: String?,
        consumerId: String?,
        amount: String?,
        purposeCode: String?,
        purposeReason: String?,
        remarks: String?,
        feeAmount: String?
    ) {
        launch {
            showLoading(true)
            val response =
                transactionsApi.bankTransferRequest(
                    beneficiaryId,
                    consumerId,
                    amount,
                    purposeCode,
                    purposeReason,
                    remarks,
                    feeAmount
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _bankTransferResponse.value = response.data.data
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

}