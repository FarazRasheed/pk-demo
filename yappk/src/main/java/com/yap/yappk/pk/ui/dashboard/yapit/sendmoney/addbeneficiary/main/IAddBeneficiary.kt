package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel

interface IAddBeneficiary {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var toolBarVisibility: MutableLiveData<Int>
        var stateList: MutableLiveData<List<AddBeneficiaryStateModel>>
        val statesAdapter: AddBeneficiaryStateAdapter
    }

    interface State : IBase.State
}