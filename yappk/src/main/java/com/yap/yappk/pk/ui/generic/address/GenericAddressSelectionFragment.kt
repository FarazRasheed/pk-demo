package com.yap.yappk.pk.ui.generic.address

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.visible
import com.digitify.core.locationX.BaseLocationFragment
import com.digitify.core.locationX.LocationModel
import com.digitify.core.locationX.OnPlaceSelectedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentGenericAddressSelectionBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.Address
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.utils.ADDRESS_DATA_MODEL
import com.yap.yappk.pk.utils.ADDRESS_MODEL
import com.yap.yappk.pk.utils.GENERIC_ADDRESS_RESULT_CODE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class GenericAddressSelectionFragment :
    BaseLocationFragment<PkFragmentGenericAddressSelectionBinding, IGenericAddressSelection.State, IGenericAddressSelection.ViewModel>(
        R.layout.pk_fragment_generic_address_selection
    ), IGenericAddressSelection.View, OnMapReadyCallback, BackNavigationResultListener,
    OnPlaceSelectedListener, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IGenericAddressSelection.ViewModel by viewModels<GenericAddressSelectionVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations
    lateinit var mapViewHandler: GenericMapViewHandler
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.addressDataModel?.value = arguments?.getParcelable(ADDRESS_DATA_MODEL)
        viewModel.address?.value = arguments?.getParcelable(ADDRESS_MODEL)
        initViews()
    }

    private fun initViews() {
        viewModel.viewState.title.value = viewModel.viewState.addressDataModel?.value?.title
        viewModel.viewState.addressLine1.value = viewModel.address?.value?.address1
        viewModel.viewState.addressLine2.value = viewModel.address?.value?.address2
        viewModel.viewState.cityName.value = viewModel.address?.value?.city
        viewModel.finalizedLocationModel?.countryName = viewModel.address?.value?.country ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initMap()
        viewModelObservers()
    }

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
                navigateBackWithResult(
                    GENERIC_ADDRESS_RESULT_CODE,
                    bundleOf(
                        GenericAddressSelectionFragment::class.java.name to Address(
                            address1 = viewModel.viewState.addressLine1.value,
                            address2 = viewModel.viewState.addressLine2.value,
                            city = viewModel.viewState.cityName.value,
                            country = viewModel.finalizedLocationModel?.countryName,
                            latitude = viewModel.finalizedLocationModel?.latLng?.latitude,
                            longitude = viewModel.finalizedLocationModel?.latLng?.longitude
                        )
                    )
                )
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

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapViewHandler = GenericMapViewHandler(mViewBinding, mMap, viewModel)
        mapViewHandler.setMapOptionOnReady()
    }

    /**
     * fun onLocationAvailable(location:Location)
     * Callback for Current Location from fused location provider.
     */
    override fun onLocationAvailable(location: Location?) {
        location?.let {
            val currentLatLng = viewModel.address?.value?.let { address ->
                LatLng(address.latitude ?: 0.0, address.longitude ?: 0.0)
            } ?: LatLng(it.latitude, it.longitude)
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
            resId = R.id.action_genericAddressSelectionFragment_to_cityNameFragment2
        )
    }

    override fun locationPermissionDeniedMessage(): String {
        return getString(R.string.screen_address_selection_location_permission_msg)
    }

    override fun permissionDeniedCommonMessage(): String =
        getString(R.string.common_text_permissions_denied)

    override fun locationPermissionOpenSettingText(): String = getString(R.string.open_setting)

    override fun onNavigationResult(result: BackNavigationResult) {
        viewModel.viewState.cityName.value =
            result.data?.getString(GenericAddressSelectionFragment::class.java.name)
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

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun viewModelObservers() {
        observe(viewModel.getDeviceLocationInformation, ::handleDeviceLocationAddress)
        observe(viewModel.getMapClickLocationInformation, ::handleMapClickLocationAddress)
    }
}