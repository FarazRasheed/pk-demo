package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.confirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.transactions.responsedtos.BankTransferResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel

interface IBankTransferAmountConfirmation {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val beneficiary: LiveData<BankTransferDataModel>
        val errorDescription: LiveData<CharSequence>
        val bankTransferResponse: LiveData<BankTransferResponse?>
        fun setBeneficiary(beneficiary: BankTransferDataModel?)
        fun bankFundsTransferRequest(
            beneficiaryId: String?,
            consumerId: String?,
            amount: String?,
            purposeCode: String?,
            purposeReason: String?,
            remarks: String?,
            feeAmount: String?
        )
    }

    interface State : IBase.State {
        var transferFee: MutableLiveData<String>
        var beneficiaryFirstName: MutableLiveData<String>
    }
}
