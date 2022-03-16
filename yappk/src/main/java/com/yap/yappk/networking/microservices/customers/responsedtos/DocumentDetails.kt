package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentDetails(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("cnic")
    var citizenNumber: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("dob")
    var dob: String? = null,
    @SerializedName("issueDate")
    var issueDate: String? = null,
    @SerializedName("expiryDate")
    var expiryDate: String? = null,
    @SerializedName("residentialAddress")
    var residentialAddress: String? = null
) : Parcelable