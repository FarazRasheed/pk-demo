package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel

interface IBeneficiaryAccountDetails {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun setBankAccountDetails(bankAccountInfo: BankData?)
        fun onTextChange(number: CharSequence, start: Int, before: Int, count: Int)
        fun createOtp(action: String)
        fun addBeneficiaryRequest(
            title: String?,
            accountNo: String?,
            bankName: String?,
            nickName: String?,
            beneficiaryType: String?
        )
        fun handleState(): ArrayList<AddBeneficiaryStateModel>
        fun handleCompleteState(): ArrayList<AddBeneficiaryStateModel>
        val bankAccountDetails: LiveData<BankData>
        val createOtpSuccess: LiveData<Boolean>
        val addBeneficiarySuccess: LiveData<Boolean>
        val errorDescription: LiveData<CharSequence>
    }

    interface State : IBase.State {
        var isValidaNickName: MutableLiveData<Boolean>
    }
}