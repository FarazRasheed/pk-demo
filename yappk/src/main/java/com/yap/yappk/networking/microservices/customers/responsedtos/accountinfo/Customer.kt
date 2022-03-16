package com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.icu.util.TimeZone
import android.os.Build
import android.os.Parcelable
import android.telephony.TelephonyManager
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Customer(

    @SerializedName("status")
    var status: String? = null,
    @SerializedName("profilePictureName")
    private var profilePictureName: String?,
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("countryCode")
    var countryCode: String,
    @SerializedName("mobileNo")
    var mobileNo: String,
    @SerializedName("customerId")
    var customerId: String? = null,
    @SerializedName("isMobileNoVerified")
    var isMobileNoVerified: String? = null,
    @SerializedName("isEmailVerified")
    var isEmailVerified: String? = null,
    @SerializedName("firstName")
    var firstName: String? = "",
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("uuid")
    var uuid: String? = "",
    @SerializedName("password")
    var password: String?,
    @SerializedName("nationality")
    var nationality: String?,
    @SerializedName("nationalityId")
    var identityNo: String?,
    @SerializedName("emailVerified")
    var emailVerified: Boolean? = false,
    @SerializedName("mobileNoVerified")
    var mobileNoVerified: Boolean? = false,
    @SerializedName("homeCountry")
    var homeCountry: String? = null,
    @SerializedName("founder")
    var founder: Boolean? = false,
    @SerializedName("customerColor")
    var customerColor: String? = null

) : Parcelable {

    fun getFullName(): String {
        return "$firstName $lastName"
    }

//    fun getFormattedPhoneNumber(context: Context): String {
//        return try {
//            val pnu = PhoneNumberUtil.getInstance()
//            val pn = pnu.parse(getCompletePhone(), getDefaultCountryCode(context))
//            return pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//    }

    @SuppressLint("DefaultLocale")
    fun getCountryCodeFromTelephony(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso.uppercase(Locale.getDefault())
    }

    fun getDefaultCountryCode(context: Context): String {
        val countryCode = getCountryCodeFromTimeZone(context)
        return if (countryCode == "") "PK" else countryCode
    }

    private fun getCountryCodeFromTimeZone(context: Context): String {
        val curTimeZoneId = Calendar.getInstance().timeZone.id
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TimeZone.getRegion(curTimeZoneId)
        } else {
            getCountryCodeFromTelephony(context)
        }
    }

    fun getPicture(): String? {
        return profilePictureName
    }

    fun setPicture(picture: String?) {
        profilePictureName = picture
    }

    val parsedColor: Int? get() = Color.parseColor(customerColor ?: "#5E35B1")

    private fun getPlusCountryCode(): String {
        return countryCode.replace("00", "+")
    }

    fun getPhoneNumberWithPlus(): String {
        return "${getPlusCountryCode()} ${mobileNo.replace(" ", "")}"
    }

    fun getPhoneNumber(): String {
        return if (countryCode.startsWith("00"))
            "$countryCode ${mobileNo.replace(" ", "")}"
        else "00$countryCode $mobileNo"
    }
}
