package com.yap.yappk.pk.utils

import com.yap.yappk.base.BaseTestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
Created by Faheem Riaz on 11/08/2021.
 **/

@ExperimentalCoroutinesApi
class PhoneUtilsTest : BaseTestCase() {
    lateinit var phoneUtils: PhoneUtils

    @Before
    fun setUp() {
        phoneUtils = PhoneUtils()
    }

    @Test
    fun testValidateIsoCountryCode() {
        val countryCodeInput = "0092"
        val actualResult = phoneUtils.getCountryCodeForRegion(countryCodeInput.toInt())

        Assert.assertEquals("PK", actualResult)
    }

    @Test
    fun testValidateMobileNumber() {
        val countryCodeInput = "0092"
        val mobileNumberInput = "3224642870"
        val actualResult = phoneUtils.isValidPhoneNumber(
            mobileNumberInput,
            phoneUtils.getCountryCodeForRegion(countryCodeInput.toInt())
        )
        Assert.assertEquals(true, actualResult)
    }

    @Test
    fun testInvalidMobileNumber() {
        val countryCodeInput = "0092"
        val mobileNumberInput = "3224642870000"
        val actualResult = phoneUtils.isValidPhoneNumber(
            mobileNumberInput,
            phoneUtils.getCountryCodeForRegion(countryCodeInput.toInt())
        )
        Assert.assertEquals(false, actualResult)
    }

    @Test
    fun testEmptyValidateMobileNumber() {
        val countryCode = ""
        val mobileNumberInput = ""
        val actualResult = phoneUtils.isValidPhoneNumber(
            mobileNumberInput,
            phoneUtils.getCountryCodeForRegion(countryCode.toIntOrNull() ?: 0)
        )
        Assert.assertEquals(false, actualResult)
    }
}