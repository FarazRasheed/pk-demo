package com.yap.yappk.pk.ui.auth.otpverification

import android.os.Build
import com.digitify.core.utils.SharedPreferenceManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
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
class LoginOTPVerificationVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var loginOtpVerificationVM: LoginOTPVerificationVM

    // Use a fake UseCase to be injected into the viewModel
    private lateinit var authApi: AuthApi
    private val customersApi: CustomersApi = mockk()

    // Mock API response
    private lateinit var verifyOTPMockResponse: BaseResponse<String>
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>

    //Mock ResourcesProvider
    private lateinit var resourcesProviders: ResourcesProviders

    // Mock SharePref
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        mockAccountsResponse = getAccountsMockResponse("accountsResponse.json")
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        sharedPreferenceManager = Mockito.mock(SharedPreferenceManager::class.java)
        authApi = Mockito.mock(AuthApi::class.java)
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)
        verifyOTPMockResponse = BaseResponse()
    }

    @Test
    fun `test otp verified if given otp is valid`() {
        val demoGraphicsRequest = DemographicDataRequest(
            "LOGIN",
            Build.VERSION.RELEASE,
            "123123-123sadasd2342d-sdasd234ds-asdas",
            Build.BRAND,
            Build.MODEL,
            "Android"
        )
        val mockVerifyOtpRequest = DemographicDataRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "123123-123sadasd2342d-sdasd234ds-asdas",
            otp = "831095"
        )

        verifyOTPMockResponse.data =
            "SD752T06aEcexwegINzFQMs5K2zI16%eyJraWQiOiI3OTE3YzAyNC0xN2M1LTRjOWYtYWI1OC05ZmFjZWI2Njg2NzYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzMjI0NjQyODcxfDgyNzk0Mzk2LTRlNDUtNDViMy05MDg5LTFmNTVmNGQ2YTk3NiIsImF1ZCI6ImUzYzdkYmFkLWYxN2YtNDhmNi05MTkzLTU3NjhjZGI0NjZlOCIsImlzcyI6Imh0dHBzOi8vc2VsZi1pc3N1ZWQubWUiLCJleHAiOjE2MzA0OTczMjYsImlhdCI6MTYzMDQ5NjEyNiwianRpIjoiOWRmMGE3ZDQtN2IzNC00NGNiLThiOWEtOTgyNDgxMGZmNTk3IiwidXQiOiJNT0JJTEVfQVBQIn0.FgvFWfPMJgAVvSLzebYP8L0_a16AM8DaCpB8M11ezPPza3Sk22gFKa13miaTyd8dNaTAHBuMP-bnekLfrZrGsdMbSBwAmpOb1aHmAhHlhnk40oHw-fN6CAEuJvIX90V-KtLXhubeWFx3sJGHRngQGZNFQXoNrdJuZHbF6RBCA2C_NOMJN_bMdJXcszp3--9hpzvp5QJbLbz1kprvofFqvpkxu_H5qVPowN3MwusBCDKnH3EzhWoxG8qyvTjrsthwVlHHZsX2RY6xgA72mIi4TjMHxkiOh9J6VM3yMdoWksTe1Gxl8ZOd-h88IjYT52tBVqLWuDXI_ge_pm-_DKxcLQ"
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Success(200, verifyOTPMockResponse)
            val resDemoGraphics = ApiResponse.Success(200, BaseApiResponse())
            val accountsResponse = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.verifyOTPForDeviceVerification(mockVerifyOtpRequest) } returns res
            coEvery { customersApi.postDemographicData(demoGraphicsRequest) } returns resDemoGraphics
            coEvery { customersApi.getAccountInfo() } returns accountsResponse
            Mockito.doNothing().`when`(authApi).setJwtToken(verifyOTPMockResponse.data!!)

            //2-Call
            loginOtpVerificationVM = LoginOTPVerificationVM(
                authApi = authApi,
                customersApi = customersApi,
                resourcesProviders = resourcesProviders,
                sharedPreferenceManager = sharedPreferenceManager,
                sessionManager = sessionManager,
                pkFeatureFlags = mockk()
            )
            loginOtpVerificationVM.verifyOtp(
                otpRequest = mockVerifyOtpRequest,
                demographicRequest = demoGraphicsRequest
            )

            //3-verify
            Assert.assertEquals(true, loginOtpVerificationVM.otpVerifiedEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `test otp verified if given otp is In-valid`() {
        val mockVerifyOtpRequest = DemographicDataRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "123123-123sadasd2342d-sdasd234ds-asdas",
            otp = "831000"
        )

        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Error(ApiError(400, "Incorrect OTP"))

            coEvery { customersApi.verifyOTPForDeviceVerification(mockVerifyOtpRequest) } returns res

            //2-Call
            loginOtpVerificationVM = LoginOTPVerificationVM(
                authApi = authApi,
                customersApi = customersApi,
                resourcesProviders = resourcesProviders,
                sharedPreferenceManager = sharedPreferenceManager,
                sessionManager = sessionManager,
                pkFeatureFlags = mockk()
            )
            loginOtpVerificationVM.verifyOtp(
                otpRequest = mockVerifyOtpRequest,
                demographicRequest = mockk()
            )

            //3-verify
            Assert.assertEquals(false, loginOtpVerificationVM.otpVerifiedEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `resend otp to mobile number success`() {
        val mockDemographicsRequest = DemographicDataRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "123123-123sadasd2342d-sdasd234ds-asdas"
        )

        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val resDemoGraphics = ApiResponse.Success(
                200,
                BaseApiResponse()
            )

            coEvery { customersApi.generateOTPForDeviceVerification(mockDemographicsRequest) } returns resDemoGraphics

            //2-Call
            loginOtpVerificationVM = LoginOTPVerificationVM(
                authApi = authApi,
                customersApi = customersApi,
                resourcesProviders = resourcesProviders,
                sharedPreferenceManager = sharedPreferenceManager,
                sessionManager = sessionManager,
                pkFeatureFlags = mockk()
            )
            loginOtpVerificationVM.resendOtp(mockDemographicsRequest)

            //3-verify
            Assert.assertEquals(true, loginOtpVerificationVM.resetOTPEvent.getOrAwaitValue())
        }
    }

    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}