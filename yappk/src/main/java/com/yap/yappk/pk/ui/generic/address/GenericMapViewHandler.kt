package com.yap.yappk.pk.ui.generic.address

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.visible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentGenericAddressSelectionBinding

class GenericMapViewHandler(
    private val mViewBinding: PkFragmentGenericAddressSelectionBinding,
    private val mMap: GoogleMap?,
    private val viewModel: IGenericAddressSelection.ViewModel
) : GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {
    var markerIcon: BitmapDescriptor? = null
    var placeAddressPhoto: Bitmap? = null

    init {
        markerIcon = getPlaceMarkerIcon()
        placeAddressPhoto = getLocationImage()
        mViewBinding.tlAddress.setEndIconOnClickListener {
            mViewBinding.etAddress.text?.clear()
        }
        mViewBinding.tlBuildingName.setEndIconOnClickListener {
            mViewBinding.etBuildingAddress.text?.clear()
        }
    }

    fun setMapOptionOnReady() {
        mMap?.setOnCameraIdleListener(this)
        mMap?.setOnCameraMoveStartedListener(this)
        mMap?.uiSettings?.isScrollGesturesEnabled = false
        mMap?.uiSettings?.isZoomGesturesEnabled = false
    }

    fun animateToExpandView() {
        mViewBinding.motionLayout.transitionToEnd()
        viewModel.isTransitionStateExpanded = true
        mMap?.uiSettings?.isScrollGesturesEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
    }

    fun animateToCollapsedView() {
        mViewBinding.motionLayout.transitionToStart()
        viewModel.isTransitionStateExpanded = false
        mMap?.uiSettings?.isScrollGesturesEnabled = false
        mMap?.uiSettings?.isZoomGesturesEnabled = false
    }

    fun placeMarkerOnMap(location: LatLng) {
        mMap?.clear()
        val markerOptions = MarkerOptions()
            .icon(markerIcon)
            .position(location)
        mMap?.addMarker(markerOptions)
    }

    override fun onCameraIdle() {
        mViewBinding.maps.ivPin.gone()
        mMap?.cameraPosition?.target?.let {
            viewModel.getLocationInfo(getContext(), it, true)
            placeMarkerOnMap(it)
            if (!viewModel.isPlaceSelected) mViewBinding.maps.ivLocation.setImageBitmap(
                placeAddressPhoto
            )
            mViewBinding.maps.ivLocationShimmer.showOriginal()
            mViewBinding.maps.tvConfirmLocationShimmer.showOriginal()
            mViewBinding.maps.tvLocationDescriptionShimmer.showOriginal()
            mViewBinding.maps.tvLocationNameShimmer.showOriginal()
        }
    }

    override fun onCameraMoveStarted(reason: Int) {
        viewModel.isPlaceSelected = false
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mMap?.clear()
            mViewBinding.maps.ivPin.visible()
            mViewBinding.maps.ivLocationShimmer.showSkeleton()
            mViewBinding.maps.tvConfirmLocationShimmer.showSkeleton()
            mViewBinding.maps.tvLocationDescriptionShimmer.showSkeleton()
            mViewBinding.maps.tvLocationNameShimmer.showSkeleton()
        }
    }

    fun isAllowedCountry(countryCode: String?): Boolean {
        if (countryCode != "PK") {
            mViewBinding.maps.clLocationCard.gone()
            mViewBinding.maps.motionLayoutMaps.transitionToEnd()
            Toast.makeText(
                getContext(),
                "Your location must be in the Pakistan.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun getPlaceMarkerIcon(): BitmapDescriptor? {
        return bitmapDescriptorFromVector(R.drawable.pk_ic_location_pin)
    }

    private fun getLocationImage(): Bitmap {
        return BitmapFactory.decodeResource(
            getContext().resources,
            R.drawable.pk_location_place_holder
        )
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(getContext(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun getContext(): Context = mViewBinding.motionLayout.context

}