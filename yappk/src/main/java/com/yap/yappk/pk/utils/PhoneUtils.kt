package com.yap.yappk.pk.utils

import android.text.TextUtils
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.inject.Inject


/**
Created by Faheem Riaz on 11/08/2021.
 **/

class PhoneUtils @Inject constructor(){

    fun isValidPhoneNumber(number: String, code: String): Boolean {
        if (TextUtils.isEmpty(number)) {
            return false
        }
        var isValid = false
        val phoneUtil = PhoneNumberUtil.getInstance()
        var isMobile: PhoneNumberUtil.PhoneNumberType? = null
        return try {
            var rawNumber = number
            if (number.startsWith("00", false)) {
                rawNumber = number.replaceFirst("00", "+")
            }
            val ph = phoneUtil.parseAndKeepRawInput(rawNumber, code)
            ph.countryCode = phoneUtil.getCountryCodeForRegion(code)
            val isPossible = phoneUtil.isPossibleNumber(ph)
            val hasDefaultCountry = code.isNotEmpty() && code != "ZZ"
            if (hasDefaultCountry) {
                isValid = phoneUtil.isValidNumberForRegion(ph, code)
            }
            isMobile = phoneUtil.getNumberType(ph)
            isValid && (PhoneNumberUtil.PhoneNumberType.MOBILE === isMobile)
        } catch (e: NumberParseException) {
            isValid
        }
    }

    fun getCountryCodeForRegion(code: Int): String {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return phoneUtil.getRegionCodeForCountryCode(code)
    }
}