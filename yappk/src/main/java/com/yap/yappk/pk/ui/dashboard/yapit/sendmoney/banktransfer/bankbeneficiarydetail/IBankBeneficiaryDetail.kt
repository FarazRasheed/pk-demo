package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary

interface IBankBeneficiaryDetail {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val beneficiary: LiveData<IBeneficiary>
        val isBeneficiaryDeleted: LiveData<Boolean>
        val isBeneficiaryUpdated: LiveData<Boolean>
        fun updateBeneficiary(beneficiaryId: String, nickName: String)
        fun setBeneficiary(beneficiary: IBeneficiary?)
        fun removeBeneficiary(beneficiaryId: String)
        fun getChooseOptionList(): ArrayList<String>
    }

    interface State : IBase.State{
        var valid: MutableLiveData<Boolean>
    }
}