package com.digitify.testyappakistan.featureflag

import com.google.gson.annotations.SerializedName

data class SPFeatureFlag(
    @SerializedName("description")
    val description: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("value")
    val value: Boolean
)
