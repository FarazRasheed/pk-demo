package com.yap.yappk.pk.ui.kyc.address.delivery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.locationX.GeoCoderUtils
import com.digitify.core.locationX.LocationAddressCallback
import com.digitify.core.locationX.LocationModel
import com.digitify.core.utils.SingleEvent
import com.google.android.gms.maps.model.LatLng
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddressSelectionVM @Inject constructor(
    override val viewState: AddressSelectionState,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) :
    BaseViewModel<IAddressSelection.State>(), IAddressSelection.ViewModel {
    private val geocoder: GeoCoderUtils = GeoCoderUtils()
    override var isTransitionStateExpanded = false
    override var isPlaceSelected: Boolean = false


    override var finalizedLocationModel: LocationModel? = null
    override var onMapClickLocationModel: LocationModel? = null

    private val _getDeviceLocationInformation: MutableLiveData<LocationModel> = MutableLiveData()
    override val getDeviceLocationInformation: LiveData<LocationModel> =
        _getDeviceLocationInformation

    private val _getMapClickLocationInformation: MutableLiveData<LocationModel> = MutableLiveData()
    override val getMapClickLocationInformation: LiveData<LocationModel> =
        _getMapClickLocationInformation

    private val _openCardOrderedSuccess: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
    override val openCardOrderedSuccess: LiveData<SingleEvent<Boolean>> = _openCardOrderedSuccess

    private val _openManualVerification: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
    override val openManualVerification: LiveData<SingleEvent<Boolean>> = _openManualVerification


    override fun onAddressConfirmed(locationModel: LocationModel?) {
        locationModel?.let {
            finalizedLocationModel = locationModel
            _getDeviceLocationInformation.value = it
        }
    }

    override fun getLocationInfo(context: Context, latLng: LatLng, onMapClick: Boolean) {
        launch {
            geocoder.execute(context, latLng, object :
                LocationAddressCallback<LocationModel> {
                override fun onAddressAvailable(response: LocationModel) {
                    if (!onMapClick)
                        _getDeviceLocationInformation.postValue(response)
                    else
                        _getMapClickLocationInformation.postValue(response)
                }

                override fun onAddressNotAvailable(errorCode: Int, reasonMsg: String) {
                    showToast(reasonMsg)
                    if (!onMapClick)
                        _getDeviceLocationInformation.postValue(null)
                    else
                        _getMapClickLocationInformation.postValue(null)
                }
            })
        }
    }

    override fun getRequestSaveAddress(): CardOrderRequest {
        return CardOrderRequest(
            address1 = viewState.addressLine1.value,
            address2 = viewState.addressLine2.value,
            city = viewState.cityName.value,
            country = finalizedLocationModel?.countryName,
            latitude = finalizedLocationModel?.latLng?.latitude,
            longitude = finalizedLocationModel?.latLng?.longitude
        )
    }

    override fun saveUserAddress(cardOrderRequest: CardOrderRequest) {
        launch {
            showLoading(true)
            val response = customersApi.saveAddressAndOrderCard(cardOrderRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        handleError(response.error.message)
                    }
                }
            }
        }
    }

    private fun handleError(message: String) {
        hideLoading()
        showAlertMessage(message)
        _openCardOrderedSuccess.value = SingleEvent(false)
        _openManualVerification.value = SingleEvent(false)
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    hideLoading()
                    if (sessionManager.userAccount.value?.isSecretQuestionVerified == true)
                        _openCardOrderedSuccess.value = SingleEvent(true)
                    else
                        _openManualVerification.value = SingleEvent(true)

                } else {
                    handleError(errorMessage)
                }
            }
        }
    }

    override fun updateLocationOnCard(locationModel: LocationModel?) {
        locationModel?.let {
            viewState.cardAddressLine1.value = getAddress1(it)
            viewState.cardAddressLine2.value = getAddress2(it)
            viewState.cardCityName.value = it.locationCityName

            onMapClickLocationModel = it
        }
    }

    override fun setUserLocation(locationModel: LocationModel?) {
        locationModel?.let {
            viewState.addressLine1.value = getAddress1(it)
            viewState.addressLine2.value = getAddress2(it)
            viewState.cityName.value = it.locationCityName
            _getMapClickLocationInformation.postValue( it)
            finalizedLocationModel = it
            onMapClickLocationModel = it
        }
    }

    private fun getAddress1(locationModel: LocationModel): String {
        val splitOn =
            if (locationModel.locationAreaName.isBlank()) locationModel.countryName else locationModel.locationAreaName

        return locationModel.locationAddress.split(splitOn)[0].replace(", $".toRegex(), "")
    }

    private fun getAddress2(it: LocationModel): String {
        val builder = StringBuilder()
        builder.append(if (it.locationAreaName.isNotBlank()) it.locationAreaName + ", " else "")
            .append(if (it.locationCityName.isNotBlank()) it.locationCityName + ", " else "")
            .append(it.countryName)

        return builder.toString()
    }

    fun onAddressFieldTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValid.value =
            validateAddress(
                s.toString(),
                viewState.addressLine2.value ?: "",
                viewState.cityName.value ?: ""
            )
    }

    fun onAddressField2TextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValid.value =
            validateAddress(
                viewState.addressLine1.value ?: "",
                s.toString(),
                viewState.cityName.value ?: ""
            )
    }

    fun onCityTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValid.value =
            validateAddress(
                viewState.addressLine1.value ?: "",
                viewState.addressLine2.value ?: "",
                s.toString()
            )
    }

    private fun validateAddress(address1: String, address2: String, city: String): Boolean {
        return address1.isNotBlank() && address2.isNotBlank() && city.isNotBlank()
    }
}