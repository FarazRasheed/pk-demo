package com.yap.yappk.pk.ui.kyc.address.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.yappk.networking.microservices.customers.responsedtos.City
import com.yap.yappk.pk.ui.kyc.address.city.adapter.CitiesAdapter

interface ICityName {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun initSearchViewListener()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val multiStateView: LiveData<MultiState>
        val citiesList: MutableLiveData<MutableList<City>>
        val citiesAdapter: CitiesAdapter
        fun onFilterCount(count: Int)
    }

    interface State : IBase.State
}