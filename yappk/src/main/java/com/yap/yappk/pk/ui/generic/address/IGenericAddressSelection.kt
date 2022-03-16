package com.yap.yappk.pk.ui.generic.address

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.locationX.LocationModel
import com.digitify.core.utils.SingleEvent
import com.google.android.gms.maps.model.LatLng
import com.yap.yappk.networking.microservices.cards.responsedtos.Address

interface IGenericAddressSelection {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val getDeviceLocationInformation: LiveData<LocationModel>
        val getMapClickLocationInformation: LiveData<LocationModel>
        val openCardOrderedSuccess: LiveData<SingleEvent<Boolean>>
        var finalizedLocationModel: LocationModel?
        var onMapClickLocationModel: LocationModel?
        var isTransitionStateExpanded: Boolean
        var isPlaceSelected: Boolean
        var address: MutableLiveData<Address>?
        fun getLocationInfo(context: Context, latLng: LatLng, onMapClick: Boolean)
        fun onAddressConfirmed(locationModel: LocationModel?)
        fun updateLocationOnCard(locationModel: LocationModel?)
        fun setUserLocation(locationModel: LocationModel?)
    }

    interface State : IBase.State {
        var title: MutableLiveData<String>
        val addressDataModel: MutableLiveData<GenericAddressDataModel>?
        val addressLine1: MutableLiveData<String>
        val addressLine2: MutableLiveData<String>
        val cityName: MutableLiveData<String>
        val cardAddressLine1: MutableLiveData<String>
        val cardAddressLine2: MutableLiveData<String>
        val cardCityName: MutableLiveData<String>
        val isValid: MutableLiveData<Boolean>
    }
}