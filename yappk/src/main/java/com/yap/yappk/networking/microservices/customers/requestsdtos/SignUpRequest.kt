package com.yap.yappk.networking.microservices.customers.requestsdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class SignUpRequest(
    @SerializedName("accountType") var accountType: String? = null,
    @SerializedName("companyName") var companyName: String? = null,
    @SerializedName("countryCode") var countryCode: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("mobileNo") var mobileNo: String? = null,
    @SerializedName("passcode") var passcode: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("whiteListed") var whiteListed: Boolean? = null
) : Serializable,Parcelable