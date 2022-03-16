package com.yap.yappk.networking.microservices.customers.responsedtos.documents

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IDCardDetect(
    @SerializedName("cnic_number")
    var citizenNumber: String? = null,
    @SerializedName("issue_date")
    var issueDate: String? = null
) : Parcelable