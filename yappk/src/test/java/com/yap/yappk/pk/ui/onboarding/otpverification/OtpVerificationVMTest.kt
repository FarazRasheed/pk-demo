package com.yap.yappk.pk.ui.onboarding.otpverification


import com.digitify.core.enums.AccountType
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile.readJsonFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.requestdtos.VerifyOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.responsedtos.OtpValidationOnBoardingResponse
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/*
[x] should verify mobile otp
[x] should verify wrong otp
[x] should recreate the otp request to mobile number
 */

@ExperimentalCoroutinesApi
class OtpVerificationVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var otpVerificationVM: OTPVerificationVM

    // Use a fake UseCase to be injected into the viewModel
    private val messagesApi: MessagesApi = mockk()

    // Mock API response
    private lateinit var verifyOTPMockResponse: OtpValidationOnBoardingResponse

    private lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        verifyOTPMockResponse = getMockResponse("mockOtpVerificationResponse.json")
    }

    @Test
    fun `verify otp onboarding mobile number success`() {
        val mockVerifyOtpRequest = VerifyOtpOnboardingRequest("0092", "3224642870", "831095")
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Success(
                200,
                OtpValidationOnBoardingResponse(data = verifyOTPMockResponse.data)
            )

            coEvery { messagesApi.verifyOtpOnboarding(mockVerifyOtpRequest) } returns res

            //2-Call
            otpVerificationVM = OTPVerificationVM(messagesApi, resourcesProviders)
            otpVerificationVM.verifyOtp(otpRequest = mockVerifyOtpRequest)

            //3-verify
            assertEquals(true, otpVerificationVM.otpEvent.getOrAwaitValue())
            assertEquals(false, otpVerificationVM.optToken.isNullOrBlank())

        }
    }


    @Test
    fun `verify otp onboarding mobile number Error`() {
        val errorResponse = ApiError(400, "Incorrect OTP")

        val mockVerifyOtpRequest = VerifyOtpOnboardingRequest("0092", "3224642870", "831099")
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Error(ApiError(400, "Incorrect OTP"))

            coEvery { messagesApi.verifyOtpOnboarding(mockVerifyOtpRequest) } returns res

            //2-Call
            otpVerificationVM = OTPVerificationVM(messagesApi, resourcesProviders)
            otpVerificationVM.verifyOtp(otpRequest = mockVerifyOtpRequest)

            //3-verify
            assertEquals(false, otpVerificationVM.otpEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `resend otp to mobile number success`() {
        val mockCreateOtpRequest = CreateOtpOnboardingRequest(
            countryCode = "0092",
            mobileNo = "3224642870",
            AccountType.B2C_ACCOUNT.name
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Success(
                200,
                OtpValidationOnBoardingResponse(data = verifyOTPMockResponse.data)
            )

            coEvery { messagesApi.createOtpOnboarding(mockCreateOtpRequest) } returns res

            //2-Call
            otpVerificationVM = OTPVerificationVM(messagesApi, resourcesProviders)
            otpVerificationVM.reCreateOtp(otpCreationRequest = mockCreateOtpRequest)

            //3-verify
            assertEquals(true, otpVerificationVM.resetOTPEvent.getOrAwaitValue())
        }
    }


    private fun getMockResponse(fileName: String): OtpValidationOnBoardingResponse {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<OtpValidationOnBoardingResponse>() {}.type
        return gson.fromJson(readJsonFile(fileName), itemType)
    }

}