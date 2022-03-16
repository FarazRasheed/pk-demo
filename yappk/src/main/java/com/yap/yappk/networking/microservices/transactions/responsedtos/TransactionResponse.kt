package com.yap.yappk.networking.microservices.transactions.responsedtos

import com.google.gson.annotations.SerializedName


data class TransactionResponse(
    @SerializedName("amount")
    val content: List<Transaction>? = null,
    @SerializedName("last")
    val last: Boolean? = null,
    @SerializedName("totalElements")
    val totalElements: Int? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("number")
    val number: Int? = null,
    @SerializedName("first")
    val first: Boolean? = null,
    @SerializedName("numberOfElements")
    val numberOfElements: Int? = null,
    @SerializedName("empty")
    val empty: Boolean? = null,
)
