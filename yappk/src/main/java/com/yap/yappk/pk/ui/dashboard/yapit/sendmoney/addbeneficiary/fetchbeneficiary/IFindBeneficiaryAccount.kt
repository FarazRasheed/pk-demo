package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.networking.microservices.customers.responsedtos.FindBankAccountInfoResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel

interface IFindBeneficiaryAccount {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
        fun initListeners()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun setBankDetails(bankData: BankData?)
        fun onTextChange(number: CharSequence, start: Int, before: Int, count: Int)
        fun fetchAccountInfoRequest(accountNo: String?, iban: String?, consumerId: String?)
        fun handleState(): ArrayList<AddBeneficiaryStateModel>
        val bankDetails: LiveData<BankData>
        val bankAccountData: LiveData<FindBankAccountInfoResponse>
        val errorDescription: LiveData<CharSequence>
    }

    interface State : IBase.State {
        var isValidaAccountHolderNumber: MutableLiveData<Boolean>
    }
}