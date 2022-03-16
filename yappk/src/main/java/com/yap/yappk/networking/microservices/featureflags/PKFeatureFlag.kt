package com.yap.yappk.networking.microservices.featureflags

import com.google.gson.annotations.SerializedName

data class PKFeatureFlag(
    @SerializedName("description")
    val description: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("value")
    val value: Boolean
)
