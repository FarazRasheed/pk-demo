package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferReasons
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel

interface IBankTransfer {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
        fun initListeners()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val beneficiary: LiveData<BankTransferDataModel>
        var transactionThreshold: MutableLiveData<TransactionThresholdResponse>?
        val errorDescription: LiveData<CharSequence>
        val reasonsList: MutableLiveData<ArrayList<FundTransferReasons?>>
        fun setBeneficiary(beneficiary: BankTransferDataModel?)
        fun onAmountChange(amount: CharSequence, start: Int, before: Int, count: Int)
        fun fetchAllInitialApis()
        fun callApiUseCase()
    }

    interface State : IBase.State {
        var isValidAmount: MutableLiveData<Boolean>
        var isFocusable: MutableLiveData<Boolean>
        var transferFee: MutableLiveData<String>
        var minLimit: MutableLiveData<Double>?
        var maxLimit: MutableLiveData<Double>?
        var accountCurrentBalance: MutableLiveData<String>?

    }
}
