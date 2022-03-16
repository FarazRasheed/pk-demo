package com.yap.yappk.networking.microservices.customers.responsedtos.documents


import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse

data class Documents(
    @SerializedName("customerUUID")
    val customerUUID: String? = null,
    @SerializedName("documentType")
    val documentType: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("nationality")
    val nationality: String? = null,
    @SerializedName("dateExpiry")
    val dateExpiry: String? = null,
    @SerializedName("dateIssue")
    val dateIssue: String? = null,
    @SerializedName("dob")
    val dob: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("passportNumber")
    val passportNumber: Any? = null,
    @SerializedName("tradeName")
    val tradeName: Any? = null,
    @SerializedName("registrationAuthority")
    val registrationAuthority: Any? = null,
    @SerializedName("licenseNumber")
    val licenseNumber: Any? = null,
    @SerializedName("legalStatus")
    val legalStatus: Any? = null,
    @SerializedName("registrationNumber")
    val registrationNumber: Any? = null,
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("customerDocuments")
    val customerDocuments: List<CustomerDocument>? = null
) : BaseApiResponse()