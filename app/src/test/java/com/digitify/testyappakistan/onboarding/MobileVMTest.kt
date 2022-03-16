package com.digitify.testyappakistan.onboarding

import android.content.Context
import com.digitify.testyappakistan.base.BaseTestCase
import com.digitify.testyappakistan.base.getOrAwaitValue
import com.digitify.testyappakistan.configurations.LoadConfig
import com.digitify.testyappakistan.onboarding.mobile.MobileNoVM
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.configurations.onboarding.PKOnboardApis
import com.yap.yappk.pk.utils.PhoneUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MobileVMTest : BaseTestCase() {

    // Subject under Test
    private lateinit var mobileNoVM: MobileNoVM

    private val phoneUtils: PhoneUtils = PhoneUtils()

    // Mock context using mockk lib
    private val mContextMock = mockk<Context>(relaxed = true)

    private val mockPkApi = mockk<PKOnboardApis>(relaxed = true)
    private val loadConfig = LoadConfig(mockPkApi, mockk())
    private val mockPkConfig = mockk<PKBuildConfigurations>(relaxed = true)



    @Before
    fun setUp() {

    }

    @Test
    fun `test valid phone number Pakistan`() {
        mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
        mobileNoVM.viewState.countryCode.set("+92")
        mobileNoVM.onPhoneNumberTextChanged("3034810336", 0, 0, 0)
        Assert.assertEquals(true, mobileNoVM.viewState.isValid.value)
    }

    @Test
    fun `test in valid phone number Pakistan`() {
        mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
        mobileNoVM.viewState.countryCode.set("+92")
        mobileNoVM.onPhoneNumberTextChanged("303481033", 0, 0, 0)
        Assert.assertEquals(false, mobileNoVM.viewState.isValid.value)
    }

    @Test
    fun `test valid phone number UAE`() {
        mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
        mobileNoVM.viewState.countryCode.set("+971")
        mobileNoVM.onPhoneNumberTextChanged("555369987", 0, 0, 0)
        Assert.assertEquals(true, mobileNoVM.viewState.isValid.value)
    }

    @Test
    fun `test in valid phone number UAE`() {
        mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
        mobileNoVM.viewState.countryCode.set("+971")
        mobileNoVM.onPhoneNumberTextChanged("5553699", 0, 0, 0)
        Assert.assertEquals(false, mobileNoVM.viewState.isValid.value)
    }

    @Test
    fun `test create otp for new user Api Success`() {

        val mockOtpOnboardingRequest = CreateOtpOnboardingRequest(
            countryCode = "0092",
            mobileNo = "3034810335"
        )
        //1 Mock Api call
        mainCoroutineRule.runBlockingTest {
            every { mockPkApi.doCreateOtp(any(), any(), any()) } answers {
                thirdArg<(Boolean) -> Unit>().invoke(true)
            }

            //2 call
            mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
            mobileNoVM.createOtp(mockOtpOnboardingRequest)

            // Create Otp
            Assert.assertEquals(true, mobileNoVM.otpCreateEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `test create otp for already registered user Api Error`() {

        val mockOtpOnboardingRequest = CreateOtpOnboardingRequest(
            countryCode = "0092",
            mobileNo = "3034810336"
        )

        //1 Mock Api call
        mainCoroutineRule.runBlockingTest {

            val response = ApiResponse.Error(
                ApiError(400, "Looks like this user already registered!")
            )

            every { mockPkApi.doCreateOtp(any(), any(), any()) } answers {
                thirdArg<(String) -> Unit>().invoke(response.error.message)
            }

            //2 call
            mobileNoVM = MobileNoVM(phoneUtils, mContextMock, mockPkConfig, loadConfig)
            mobileNoVM.createOtp(mockOtpOnboardingRequest)

            // Create Otp
            Assert.assertEquals(false, mobileNoVM.otpCreateEvent.getOrAwaitValue())
        }
    }
}