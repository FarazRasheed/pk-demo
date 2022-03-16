package com.yap.yappk.networking.microservices.customers.responsedtos.documents


import com.google.gson.annotations.SerializedName

data class CustomerDocument(
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("contentType")
    val contentType: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("customerId")
    val customerId: String? = null,
    @SerializedName("customerUUID")
    val customerUUID: String? = null,
    @SerializedName("documentInformation")
    val documentInformation: DocumentInformation? = null,
    @SerializedName("documentType")
    val documentType: String? = null,
    @SerializedName("expired")
    val expired: Boolean? = null,
    @SerializedName("pageNo")
    val pageNo: Int? = null,
    @SerializedName("updatedBy")
    val updatedBy: String? = null,
    @SerializedName("updatedDate")
    val updatedDate: String? = null,
    @SerializedName("uploadDate")
    val uploadDate: String? = null
)