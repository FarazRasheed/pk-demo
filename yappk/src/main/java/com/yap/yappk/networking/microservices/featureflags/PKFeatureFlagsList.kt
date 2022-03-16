package com.yap.yappk.networking.microservices.featureflags

import com.google.gson.annotations.SerializedName

data class PKFeatureFlagsList(
    @SerializedName("features")
    val features: List<PKFeatureFlag>

)
