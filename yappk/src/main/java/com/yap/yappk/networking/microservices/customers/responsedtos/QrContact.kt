package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrContact(
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String? = "",
    @SerializedName("countryCode")
    var countryCode: String? = "",
    @SerializedName("mobileNo")
    var mobileNo: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("profilePictureName")
    var profilePictureName: String? = "",
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = "",
    @SerializedName("uuid")
    val uuid: String? = null,
) : IBeneficiary, Parcelable {
    override val fullName: String?
        get() = "$firstName $lastName"

    override val subtitle: String?
        get() = mobileNo

    override val userType: String
        get() = "Y2Y"

    override val isYapUser: Boolean
        get() = true

    override val accountUUID: String
        get() = uuid ?: ""

    override val imgUrl: String?
        get() = profilePictureName

    override val creationDateOfBeneficiary: String
        get() = beneficiaryCreationDate ?: ""

    override val mobileNumber: String? get() = mobileNo
    override val countryCde: String? get() = countryCode
    fun fullName() = "$firstName $lastName"
}
