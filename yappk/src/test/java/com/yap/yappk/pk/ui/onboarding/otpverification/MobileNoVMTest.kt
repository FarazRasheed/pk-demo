package com.yap.yappk.pk.ui.onboarding.otpverification//package com.yap.yappk.pk.ui.onboarding.otpverification
//
//import com.yap.networking.base.BaseTestCase
//import com.yap.yappk.pk.utils.PhoneUtils
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//
//
///**
//Created by Faheem Riaz on 12/08/2021.
// **/
//
//@ExperimentalCoroutinesApi
//class MobileNoVMTest : BaseTestCase() {
//
//    lateinit var OTPVerificationVM: OTPVerificationVM
//
//    @Before
//    fun setUp() {
//        val phoneUtils = PhoneUtils()
//        OTPVerificationVM = OTPVerificationVM(phoneUtils)
//    }
//
//    @Test
//    fun testValidateMobileNumber() {
//        // Given
//        val mobileNumberInput = "3224642870"
//
//        // When
//        OTPVerificationVM.onPhoneNumberTextChanged(mobileNumberInput, 0, 0, 0)
//
//        // Then
//        OTPVerificationVM.viewState.isValid.observeForever {
//            Assert.assertEquals(true, it)
//        }
//    }
//
//    @Test
//    fun testInvalidMobileNumber() {
//        // Given
//        val mobileNumberInput = "3224642870000"
//
//        // When
//        OTPVerificationVM.onPhoneNumberTextChanged(mobileNumberInput, 0, 0, 0)
//
//        // Then
//        OTPVerificationVM.viewState.isValid.observeForever {
//            Assert.assertEquals(false, it)
//        }
//    }
//
//    @Test
//    fun testEmptyValidateMobileNumber() {
//        // Given
//        val mobileNumberInput = ""
//
//        // When
//        OTPVerificationVM.onPhoneNumberTextChanged(mobileNumberInput, 0, 0, 0)
//
//        // Then
//        OTPVerificationVM.viewState.isValid.observeForever {
//            Assert.assertEquals(false, it)
//        }
//    }
//
//}