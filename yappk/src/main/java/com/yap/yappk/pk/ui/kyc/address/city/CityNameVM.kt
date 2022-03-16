package com.yap.yappk.pk.ui.kyc.address.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.City
import com.yap.yappk.pk.ui.kyc.address.city.adapter.CitiesAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityNameVM @Inject constructor(
    override val viewState: CityNameState,
    private val customersApi: CustomersApi
) : BaseViewModel<ICityName.State>(), ICityName.ViewModel {
    override val citiesAdapter: CitiesAdapter = CitiesAdapter(mutableListOf())

    private val _multiStateView: MutableLiveData<MultiState> = MutableLiveData()
    override val multiStateView: LiveData<MultiState> = _multiStateView

    private val _citiesList: MutableLiveData<MutableList<City>> = MutableLiveData()
    override val citiesList: MutableLiveData<MutableList<City>> = _citiesList

    override fun onCreate() {
        super.onCreate()
        getCities()
    }

    private fun getCities() {
        launch {
            _multiStateView.postValue(MultiState.loading(null))
            when (val response = customersApi.getCities()) {
                is ApiResponse.Success -> {
                    _citiesList.postValue(response.data.data)
                    _multiStateView.postValue(MultiState.success(null))
                }
                is ApiResponse.Error -> {
                    _multiStateView.postValue(MultiState.error(null))
                    showToast(message = response.error.message, onBackGround = true)
                }
            }
        }
    }

    override fun onFilterCount(count: Int) {
        _multiStateView.value = if (count == 0) MultiState.empty(null) else MultiState.success(null)
    }
}