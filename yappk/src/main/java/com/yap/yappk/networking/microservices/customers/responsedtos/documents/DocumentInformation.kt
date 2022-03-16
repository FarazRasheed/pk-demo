package com.yap.yappk.networking.microservices.customers.responsedtos.documents


import com.google.gson.annotations.SerializedName

data class DocumentInformation(
    @SerializedName("accountStatus")
    val accountStatus: String? = null,
    @SerializedName("accountType")
    val accountType: String? = null,
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("countryIsSanctioned")
    val countryIsSanctioned: Boolean? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("customerUUID")
    val customerUUID: String? = null,
    @SerializedName("dateExpiry")
    val dateExpiry: String? = null,
    @SerializedName("dateIssue")
    val dateIssue: String? = null,
    @SerializedName("dob")
    val dob: String? = null,
    @SerializedName("documentType")
    val documentType: String? = null,
    @SerializedName("eid")
    val eid: String? = null,
    @SerializedName("expiryUpdated")
    val expiryUpdated: Boolean? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("identityNo")
    val identityNo: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("middleName")
    val middleName: String? = null,
    @SerializedName("nationality")
    val nationality: String? = null,
    @SerializedName("updatedBy")
    val updatedBy: String? = null,
    @SerializedName("updatedDate")
    val updatedDate: String? = null
)