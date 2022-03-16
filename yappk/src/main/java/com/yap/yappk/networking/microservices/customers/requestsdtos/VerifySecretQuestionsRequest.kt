package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName

data class VerifySecretQuestionsRequest(
    @SerializedName("motherMaidenName")
    val motherMaidenName: String? = null,
    @SerializedName("cityOfBirth")
    val cityOfBirth: String? = null
)