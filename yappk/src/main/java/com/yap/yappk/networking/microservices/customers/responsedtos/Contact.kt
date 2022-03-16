package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("countryCode")
    var countryCode: String? = "",
    @SerializedName("mobileNo")
    var mobileNo: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("beneficiaryPictureUrl")
    var beneficiaryPictureUrl: String? = "",
    @SerializedName("yapUser")
    var yapUser: Boolean? = false,
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = "",
    @SerializedName("accountDetailList")
    val accountDetailList: List<ContactAccountDetail>? = null,
) : IBeneficiary, Parcelable {
    override val fullName: String?
        get() = title

    override val subtitle: String?
        get() = mobileNo

    override val userType: String?
        get() = if (yapUser == true) "Y2Y" else ""

    override val isYapUser: Boolean
        get() = yapUser == true

    override val accountUUID: String
        get() = this.accountDetailList?.first()?.accountUuid ?: ""

    override val imgUrl: String?
        get() = beneficiaryPictureUrl

    override val creationDateOfBeneficiary: String
        get() = beneficiaryCreationDate ?: ""

    override val mobileNumber: String? get() = mobileNo
    override val countryCde: String? get() = countryCode
    fun fullName() = "$title"
}
