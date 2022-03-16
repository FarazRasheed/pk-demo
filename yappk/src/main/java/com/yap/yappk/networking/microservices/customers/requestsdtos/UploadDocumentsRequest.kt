package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName
import java.util.*

data class UploadDocumentsRequest(
    @SerializedName("filePaths")
    val filePaths: List<String>? = null,
    @SerializedName("documentType")
    val documentType: String? = null,
    @SerializedName("dateExpiry")
    val dateExpiry: Date? = null,
    @SerializedName("dateIssue")
    val dateIssue: Date? = null,
    @SerializedName("dob")
    val dob: Date? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("gender")
    val gender: String? = null, // M/F
    @SerializedName("identityNo")
    val identityNo: String? = null,
    @SerializedName("nationality")
    val nationality: String? = null
)