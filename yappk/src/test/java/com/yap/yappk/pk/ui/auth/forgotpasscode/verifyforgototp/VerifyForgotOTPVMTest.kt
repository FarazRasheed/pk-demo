package com.yap.yappk.pk.ui.auth.forgotpasscode.verifyforgototp

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.networking.microservices.messages.responsedtos.ForgotPasscodeOtpResponse
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class VerifyForgotOTPVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var verifyForgotOTPVM: VerifyForgotOTPVM

    // Use a fake UseCase to be injected into the viewModel
    private val messagesApi: MessagesApi = mockk()

    // Mock API responses
    private lateinit var mockForgotOtpResponse: ForgotPasscodeOtpResponse
    private lateinit var mockVerifyForgotOtpResponse: ForgotPasscodeOtpResponse

    //Mock ResourcesProvider
    private lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        mockForgotOtpResponse = getForgotOtpMockResponse("createForgotOtpResponse.json")
        mockVerifyForgotOtpResponse = getForgotOtpMockResponse("verifyForgotOtpResponse.json")
    }

    @Test
    fun `test resend otp forgot api success`() {
        //Given
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, mockForgotOtpResponse)
            coEvery { messagesApi.createForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            verifyForgotOTPVM =
                VerifyForgotOTPVM(
                    viewState = VerifyForgotOTPState(),
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi
                )
            //2-Call
            verifyForgotOTPVM.resendOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(true, verifyForgotOTPVM.isForgotOtpCreated.getOrAwaitValue())
        }
    }

    @Test
    fun `test resend forgot otp api error`() {
        //Given
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { messagesApi.createForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            verifyForgotOTPVM =
                VerifyForgotOTPVM(
                    viewState = VerifyForgotOTPState(),
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi
                )
            //2-Call
            verifyForgotOTPVM.resendOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(false, verifyForgotOTPVM.isForgotOtpCreated.getOrAwaitValue())
        }
    }

    @Test
    fun `test verify otp forgot api success`() {
        //Given
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false,
            otp = "2233"
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, mockVerifyForgotOtpResponse)
            coEvery { messagesApi.verifyForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            verifyForgotOTPVM =
                VerifyForgotOTPVM(
                    viewState = VerifyForgotOTPState(),
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi
                )
            //2-Call
            verifyForgotOTPVM.verifyOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(true, verifyForgotOTPVM.isForgotOtpVerified.getOrAwaitValue())
        }
    }

    @Test
    fun `test verify forgot otp api error`() {
        //Given
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false,
            otp = "3322"
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { messagesApi.verifyForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            verifyForgotOTPVM =
                VerifyForgotOTPVM(
                    viewState = VerifyForgotOTPState(),
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi
                )
            //2-Call
            verifyForgotOTPVM.verifyOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(false, verifyForgotOTPVM.isForgotOtpVerified.getOrAwaitValue())
        }
    }


    private fun getForgotOtpMockResponse(fileName: String): ForgotPasscodeOtpResponse {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<ForgotPasscodeOtpResponse>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }

}
