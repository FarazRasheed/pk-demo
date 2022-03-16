package com.yap.yappk.networking.microservices.customers.requestsdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AddBeneficiaryRequest(
    @SerializedName("title")
    val title: String?,
    @SerializedName("accountNo")
    val accountNo: String?,
    @SerializedName("bankName")
    val bankName: String?,
    @SerializedName("nickName")
    val nickName: String?,
    @SerializedName("beneficiaryType")
    val beneficiaryType: String?
) : Serializable, Parcelable
