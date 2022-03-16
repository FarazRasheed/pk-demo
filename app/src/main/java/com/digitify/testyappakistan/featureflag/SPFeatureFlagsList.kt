package com.digitify.testyappakistan.featureflag

import com.google.gson.annotations.SerializedName

data class SPFeatureFlagsList(
    @SerializedName("features")
    val features: List<SPFeatureFlag>
)
