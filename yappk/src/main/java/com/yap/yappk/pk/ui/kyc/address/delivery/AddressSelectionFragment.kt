package com.yap.yappk.pk.ui.kyc.address.delivery

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.extensions.visible
import com.digitify.core.locationX.BaseLocationFragment
import com.digitify.core.locationX.LocationModel
import com.digitify.core.locationX.OnPlaceSelectedListener
import com.digitify.core.utils.SingleEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.yap.uikit.widget.progressbar.AnimatedProgressBar
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAddressSelectionBinding
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.ui.kyc.paymentconfirmation.CardOrderPaymentConfirmationFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddressSelectionFragment :
    BaseLocationFragment<FragmentAddressSelectionBinding, IAddressSelection.State, IAddressSelection.ViewModel>(
        R.layout.fragment_address_selection
    ), IAddressSelection.View, OnMapReadyCallback, BackNavigationResultListener,
    OnPlaceSelectedListener, AnimatedProgressBar.OnProgressWidgetButtonsClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IAddressSelection.ViewModel by viewModels<AddressSelectionVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations
    lateinit var mapViewHandler: MapViewHandler
    private var mMap: GoogleMap? = null

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvMapConfirmLocation -> {
                mapViewHandler.animateToExpandView()
            }
            R.id.tvConfirmLocation -> {
                viewModel.onAddressConfirmed(viewModel.onMapClickLocationModel)
                mapViewHandler.animateToCollapsedView()
            }
            R.id.ivClose -> {
                mapViewHandler.animateToCollapsedView()
            }
            R.id.btnNext -> {
                navigateToOrderScreen()
            }
            R.id.ivSearch -> {
                showPlacesSearchScreen(
                    pkBuildConfigurations.configManager?.googleMapsApiKey ?: "",
                    "PK",
                    this
                )
            }
            R.id.etCityName -> {
                openCitySelectionScreen()
            }
            R.id.ivGps -> {
                getLastKnownLocation()
            }
        }
    }

    private fun navigateToOrderScreen() {
        parentViewModel.cardDeliveryAddress = viewModel.getRequestSaveAddress()
        navigate(
            R.id.action_addressSelectionFragment_to_cardOrderPaymentConfirmationFragment, bundleOf(
                CardOrderPaymentConfirmationFragment::class.java.name to parentViewModel.cardDeliveryAddress
            )
        )
    }

    private fun savAddressApiCall() {
        viewModel.saveUserAddress(viewModel.getRequestSaveAddress())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        viewModelObservers()
        mViewBinding.apbProgress.setOnWidgetClickListener(this)
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapViewHandler = MapViewHandler(mViewBinding, mMap, viewModel)
        mapViewHandler.setMapOptionOnReady()
    }

    /**
     * fun onLocationAvailable(location:Location)
     * Callback for Current Location from fused location provider.
     */
    override fun onLocationAvailable(location: Location?) {
        location?.let {
            val currentLatLng = LatLng(it.latitude, it.longitude)
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            viewModel.getLocationInfo(
                context = requireContext(),
                latLng = currentLatLng,
                onMapClick = false
            )
            stopLocationUpdates()
            mapViewHandler.placeMarkerOnMap(currentLatLng)
        }
    }

    /**
     * fun handleDeviceLocationAddress(location:Location)
     * Observer for user current location and user final selected location.
     * note : final location .
     */
    private fun handleDeviceLocationAddress(locationModel: LocationModel?) {
        if (mapViewHandler.isAllowedCountry(locationModel?.countryCode)) {
            mViewBinding.maps.clLocationCard.visible()
            locationModel.let {
                viewModel.setUserLocation(locationModel)
            }
        }
    }

    /**
     * fun handleMapClickLocationAddress(location:Location)
     * Observer for finding and searching Location from map
     * note : Not final location.
     */
    private fun handleMapClickLocationAddress(locationModel: LocationModel?) {
        if (mapViewHandler.isAllowedCountry(locationModel?.countryCode)) {
            mViewBinding.maps.motionLayoutMaps.transitionToStart()
            mViewBinding.maps.clLocationCard.visible()
            viewModel.updateLocationOnCard(locationModel)
        }
    }

    private fun openCitySelectionScreen() {
        navigateForResult(
            requestCode = 1,
            resId = R.id.action_addressSelectionFragment_to_cityNameFragment
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.getDeviceLocationInformation, ::handleDeviceLocationAddress)
        observe(viewModel.getMapClickLocationInformation, ::handleMapClickLocationAddress)
    }

    override fun locationPermissionDeniedMessage(): String {
        return getString(R.string.screen_address_selection_location_permission_msg)
    }

    override fun permissionDeniedCommonMessage(): String =
        getString(R.string.common_text_permissions_denied)

    override fun locationPermissionOpenSettingText(): String = getString(R.string.open_setting)

    override fun onNavigationResult(result: BackNavigationResult) {
        viewModel.viewState.cityName.value =
            result.data?.getString(AddressSelectionFragment::class.java.name)
    }

    /**
     * fun onPlaceSelected(place: Place)
     * Callback for place selected by user from search location list.
     */
    override fun onPlaceSelected(place: Place) {
        place.latLng?.let {
            viewModel.getLocationInfo(requireContext(), it, true)
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
            mapViewHandler.placeMarkerOnMap(it)
            getPlacePhoto(place) { bitmap ->
                bitmap?.let { placePhoto ->
                    viewModel.isPlaceSelected = true
                    mViewBinding.maps.ivLocation.setImageBitmap(placePhoto)
                } ?: mViewBinding.maps.ivLocation.setImageBitmap(mapViewHandler.placeAddressPhoto)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (viewModel.isTransitionStateExpanded) {
            mapViewHandler.animateToCollapsedView()
            false
        } else
            true
    }

    override fun onBackButtonClicked() {
        navigateBack()
    }
}