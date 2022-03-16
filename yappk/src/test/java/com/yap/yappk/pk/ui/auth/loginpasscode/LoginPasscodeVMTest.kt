package com.yap.yappk.pk.ui.auth.loginpasscode

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.authentication.requestdtos.LoginRequest
import com.yap.yappk.networking.microservices.authentication.responsedtos.LoginResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.networking.microservices.messages.responsedtos.ForgotPasscodeOtpResponse
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.mock


@ExperimentalCoroutinesApi
class LoginPasscodeVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var loginPasscodeVM: LoginPasscodeVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()
    private val authApi: AuthApi = mockk()
    private val messagesApi: MessagesApi = mockk()

    // Mock API responses
    private lateinit var mockLoginResponse: LoginResponse
    private lateinit var mockNewLoginResponse: LoginResponse
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>
    private lateinit var mockForgotOtpResponse: ForgotPasscodeOtpResponse

    //Mock ResourcesProvider
    private lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        resourcesProviders = mock(ResourcesProviders::class.java)
        mockLoginResponse = getMockLoginResponse()
        mockNewLoginResponse = getMockNewUserLoginResponse()
        mockAccountsResponse = getAccountsMockResponse("accountsResponse.json")
        mockForgotOtpResponse = getCreateForgotOtpMockResponse("createForgotOtpResponse.json")
    }

    @Test
    fun `test login if user is already logged in`() {
        //Given
        val mockLoginRequest = LoginRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "12312-012312312-3-123123-asd"
        )
        // Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Success(200, mockLoginResponse)
            val accountsResponse = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { authApi.login(mockLoginRequest) } returns res
            coEvery { customersApi.getAccountInfo() } returns accountsResponse

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = authApi,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk(),
                    sessionManager = SessionManager(customersApi, mockk(), mockk()),
                    resourcesProviders = mockk(),
                    messagesApi = mockk(),
                    pkFeatureFlags = mockk()
                )
            loginPasscodeVM.login(mockLoginRequest)

            // Verify
            Assert.assertEquals(true, loginPasscodeVM.isDeviceValidate.getOrAwaitValue())
        }
    }

    @Test
    fun `test login if new user is logged in`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockLoginRequest = LoginRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "12ass312-012assa312312-3-123asa123-asd"
        )
        val mockDemographicsRequest = DemographicDataRequest(
            clientId = mockLoginRequest.clientId,
            clientSecret = mockLoginRequest.clientSecret,
            deviceId = mockLoginRequest.deviceId
        )

        // Mock calls
        mainCoroutineRule.runBlockingTest {
            val res = ApiResponse.Success(200, mockNewLoginResponse)
            val resDemoGraphics = ApiResponse.Success(200, BaseApiResponse())
            coEvery { authApi.login(mockLoginRequest) } returns res
            coEvery { customersApi.getAccountInfo() } returns mockk()
            coEvery { customersApi.generateOTPForDeviceVerification(mockDemographicsRequest) } returns resDemoGraphics

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = authApi,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = mockk(),
                    messagesApi = mockk(),
                    pkFeatureFlags = mockk()
                )
            loginPasscodeVM.login(mockLoginRequest)

            // Verify
            Assert.assertEquals(false, loginPasscodeVM.isDeviceValidate.getOrAwaitValue())
        }
    }

    @Test
    fun `test login if user tries too many incorrect attempts with error code 302`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockLoginRequest = LoginRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "12ass312-012assa312312-3-123asa123-asd"
        )

        // Mock calls
        mainCoroutineRule.runBlockingTest {
            val mockApiResponse = ApiResponse.Error(
                ApiError(
                    400,
                    "Your account is blocked. Please contact system administrator.",
                    "302"
                )
            )
            coEvery { authApi.login(mockLoginRequest) } returns mockApiResponse
            Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
                .thenReturn("Sorry, that’s too many incorrect attempts. Click on Forgot passcode to set a new one.")
            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = authApi,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = resourcesProviders,
                    messagesApi = mockk(),
                    pkFeatureFlags = mockk()
                )
            loginPasscodeVM.login(mockLoginRequest)

            // Verify
            Assert.assertEquals(true, loginPasscodeVM.viewState.isScreenLocked.getOrAwaitValue())
            Assert.assertEquals(true, loginPasscodeVM.viewState.isAccountLocked.getOrAwaitValue())
            Assert.assertEquals(
                "Sorry, that’s too many incorrect attempts. Click on Forgot passcode to set a new one.",
                loginPasscodeVM.viewState.passcodeError.getOrAwaitValue()
            )
        }
    }

    @Test
    fun `test login if user tries too many incorrect attempts with error code 303`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockLoginRequest = LoginRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "12ass312-012assa312312-3-123asa123-asd"
        )

        // Mock calls
        mainCoroutineRule.runBlockingTest {
            val mockApiResponse = ApiResponse.Error(ApiError(400, "120", "303"))
            coEvery { authApi.login(mockLoginRequest) } returns mockApiResponse

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = authApi,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = resourcesProviders,
                    messagesApi = mockk(),
                    pkFeatureFlags = mockk()
                )
            loginPasscodeVM.login(mockLoginRequest)

            // Verify
            Assert.assertEquals(true, loginPasscodeVM.viewState.isScreenLocked.getOrAwaitValue())
            Assert.assertEquals(false, loginPasscodeVM.viewState.isAccountLocked.getOrAwaitValue())
        }
    }

    @Test
    fun `test login if user tries too many incorrect attempts with error code 1260`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockLoginRequest = LoginRequest(
            clientId = "3224642870",
            clientSecret = "1212",
            deviceId = "12ass312-012assa312312-3-123asa123-asd"
        )

        // Mock calls
        mainCoroutineRule.runBlockingTest {
            val mockApiResponse = ApiResponse.Error(
                ApiError(
                    400,
                    "Your account is blocked. Please contact system administrator.",
                    "1260"
                )
            )
            coEvery { authApi.login(mockLoginRequest) } returns mockApiResponse

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = authApi,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = resourcesProviders,
                    messagesApi = mockk(),
                    pkFeatureFlags = mockk()
                )
            loginPasscodeVM.login(mockLoginRequest)

            // Verify
            Assert.assertEquals(true, loginPasscodeVM.viewState.isScreenLocked.getOrAwaitValue())
            Assert.assertEquals(true, loginPasscodeVM.viewState.isAccountLocked.getOrAwaitValue())
            Assert.assertEquals(
                "Your account is blocked. Please contact system administrator.",
                loginPasscodeVM.viewState.passcodeError.getOrAwaitValue()
            )
        }
    }

    @Test
    fun `test create forgot api success`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, mockForgotOtpResponse)
            coEvery { messagesApi.createForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = mockk(),
                    customersApi = mockk(),
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi,
                    pkFeatureFlags = mockk()
                )
            //2-Call
            loginPasscodeVM.createForgotOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(true, loginPasscodeVM.isForgotOtpCreated.getOrAwaitValue())
        }
    }

    @Test
    fun `test create forgot api error`() {
        //Given
        val sessionManager = mock(SessionManager::class.java)
        val mockCreateForgotPasscodeOtpRequest = ForgotPasscodeOtpRequest(
            destination = "009230342955",
            emailOTP = false
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { messagesApi.createForgotPasscodeOTP(mockCreateForgotPasscodeOtpRequest) } returns response

            // Call VM
            loginPasscodeVM =
                LoginPasscodeVM(
                    viewState = LoginPasscodeState(),
                    authApi = mockk(),
                    customersApi = mockk(),
                    sharedPreferenceManager = mockk(),
                    sessionManager = sessionManager,
                    resourcesProviders = resourcesProviders,
                    messagesApi = messagesApi,
                    pkFeatureFlags = mockk()
                )
            //2-Call
            loginPasscodeVM.createForgotOtp(mockCreateForgotPasscodeOtpRequest)

            //Verify
            Assert.assertEquals(false, loginPasscodeVM.isForgotOtpCreated.getOrAwaitValue())
        }
    }

    private fun getMockLoginResponse(): LoginResponse {
        return LoginResponse(
            accessToken = "ajsdiaji2342ih42oih423iohsddsahoiw",
            idToken = "qeqwehqwiuehwquiehui23432iuhg4uhgwdjdsa"
        )
    }

    private fun getMockNewUserLoginResponse(): LoginResponse {
        return LoginResponse(
            accessToken = null,
            idToken = null
        )
    }

    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }

    private fun getCreateForgotOtpMockResponse(fileName: String): ForgotPasscodeOtpResponse {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<ForgotPasscodeOtpResponse>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}