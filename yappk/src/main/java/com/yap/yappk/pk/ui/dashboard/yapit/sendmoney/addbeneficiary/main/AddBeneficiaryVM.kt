package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBeneficiaryVM @Inject constructor(override val viewState: AddBeneficiaryState) :
    BaseViewModel<IAddBeneficiary.State>(), IAddBeneficiary.ViewModel {
    override var toolBarVisibility: MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    override var stateList: MutableLiveData<List<AddBeneficiaryStateModel>> = MutableLiveData()
    override val statesAdapter: AddBeneficiaryStateAdapter = AddBeneficiaryStateAdapter(mutableListOf())
}