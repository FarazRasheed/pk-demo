package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class BankBeneficiary(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("beneficiaryType")
    var beneficiaryType: String? = null,
    @SerializedName("profilePictureUrl")
    var profilePictureUrl: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("bankLogoUrl")
    var bankLogoUrl: String? = null,
    @SerializedName("bankName")
    var bankName: String? = null,
    @SerializedName("iban")
    var iban: String? = null,
    @SerializedName("nickName")
    var nickName: String? = null,
    @SerializedName("beneficiaryCreationDate")
    var beneficiaryCreationDate: String? = null,
    @SerializedName("accountNo")
    var accountNo: String? = "",
    @Transient
    var imageUri: File? = null

) : IBeneficiary, Parcelable {
    override val fullName: String?
        get() = title

    override val subtitle: String?
        get() = nickName

    override val userType: String?
        get() = beneficiaryType

    override val isYapUser: Boolean
        get() = beneficiaryType == "Y2Y"

    override val accountUUID: String
        get() = accountNo ?: iban ?: ""

    override val imgUrl: String?
        get() = profilePictureUrl

    override val creationDateOfBeneficiary: String
        get() = beneficiaryCreationDate ?: ""

    override val bankImgUrl: String?
        get() = bankLogoUrl

    override val beneficiaryId: String?
        get() = id

    override val bankTitle: String?
        get() = bankName

    override val ibanNumber: String?
        get() = iban

    override val accountNumber: String?
        get() = accountNo

    override val imgUri: File?
        get() = imageUri

    fun fullName() = title
}