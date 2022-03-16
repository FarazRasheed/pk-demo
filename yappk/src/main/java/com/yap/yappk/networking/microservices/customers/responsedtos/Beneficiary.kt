package com.yap.yappk.networking.microservices.customers.responsedtos

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Keep
@Parcelize
data class Beneficiary(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("accountUuid")
    var accountUuid: String? = null,
    @SerializedName("beneficiaryUuid")
    var beneficiaryUuid: String? = null,
    @SerializedName("beneficiaryAccountType")
    var beneficiaryAccountType: String? = null,
    @SerializedName("beneficiaryType")
    var beneficiaryType: String? = null,
    @SerializedName("beneficiaryPictureUrl")
    var beneficiaryPictureUrl: String? = null,
    @SerializedName("mobileNo")
    var mobileNo: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("lastUsedDate")
    var lastUsedDate: String? = null,
    @SerializedName("firstName")
    var firstName: String? = null,
    @SerializedName("lastName")
    var lastName: String? = null,
    @SerializedName("sanctioned")
    var sanctioned: Boolean? = null,
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = null,
    @SerializedName("cbwsicompliant")
    var cbwsicompliant: Boolean? = null,
    @SerializedName("countryCode")
    var countryCode: String? = ""

) : IBeneficiary, Parcelable {
    override val fullName: String?
        get() = title

    override val beneficiaryId: String
        get() = id.toString()

    override val subtitle: String?
        get() = if (beneficiaryType == "Y2Y") mobileNo else fullName()

    override val userType: String?
        get() = beneficiaryType

    override val isYapUser: Boolean
        get() = beneficiaryType == "Y2Y"

    override val accountUUID: String
        get() = accountUuid ?: ""

    override val imgUrl: String?
        get() = beneficiaryPictureUrl

    override val creationDateOfBeneficiary: String
        get() = beneficiaryCreationDate ?: ""
    override val countryCde: String get() = countryCode ?: ""
    override val mobileNumber: String? get() = mobileNo

    fun fullName() = "$firstName $lastName"
}

interface IBeneficiary : IYapUser, Parcelable {
    val fullName: String? get() = null
    val subtitle: String? get() = null
    val userType: String? get() = null
    val imgUrl: String? get() = null
    val imgUri: File? get() = null
    val countryCde: String? get() = null
    val bankImgUrl: String? get() = null
    val bankTitle: String? get() = null
    val accountNumber: String? get() = null
    val ibanNumber: String? get() = null
    val beneficiaryId: String? get() = null
}

interface IYapUser {
    val isYapUser: Boolean get() = false
    val accountUUID: String get() = ""
    val creationDateOfBeneficiary: String get() = ""
    val mobileNumber: String? get() = null
}