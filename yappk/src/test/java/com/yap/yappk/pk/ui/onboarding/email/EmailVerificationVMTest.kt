package com.yap.yappk.pk.ui.onboarding.email

import android.os.Build
import com.digitify.core.adjustRefferal.ReferralInfo
import com.digitify.core.enums.AccountType
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.UtilityFunctions
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
import com.yap.yappk.networking.microservices.customers.requestsdtos.EmailVerificationRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.SaveReferralRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.SignUpRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


@ExperimentalCoroutinesApi
class EmailVerificationVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var emailVerificationVM: EmailVerificationVM
    private lateinit var utils: UtilityFunctions

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()
    private val authApi: AuthApi = mockk()

    // Mock API responses
    private lateinit var mockVerifyEmailResponse: BaseResponse<String>
    private lateinit var mockSignupResponse: BaseResponse<String>
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>

    // Mock SharePref
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    @Before
    fun setUp() {
        sharedPreferenceManager = Mockito.mock(SharedPreferenceManager::class.java)

        mockVerifyEmailResponse = getEmailMockResponse("verifyEmailResponse.json")
        mockSignupResponse = getSignupMockResponse("signupResponse.json")
        mockAccountsResponse = getAccountsMockResponse("accountsResponse.json")
        utils = UtilityFunctions()
    }

    @Test
    fun testMockSharedPref() {
        Mockito.`when`(sharedPreferenceManager.getReferralInfo())
            .thenReturn(ReferralInfo("12", "12312312"))

        assertEquals(ReferralInfo("12", "12312312"), sharedPreferenceManager.getReferralInfo())
    }

    @Test
    fun `test signup user successfully if given information is correct `() {
        //Given
        val mockEmailRequest =
            EmailVerificationRequest(
                email = "faheem@digitify.com",
                accountType = AccountType.B2C_ACCOUNT.name,
                token = "213123123123"
            )
        val mockSignUpRequest = SignUpRequest(
            email = "faheem@digitify.com",
            countryCode = "0092",
            whiteListed = false,
            accountType = AccountType.B2C_ACCOUNT.name
        )
        val mockDemoGraphicsRequest = DemographicDataRequest(
            "SIGNUP",
            Build.VERSION.RELEASE,
            "1212121",
            Build.BRAND,
            Build.MODEL,
            "Android"
        )
        val mockReferralRequest = SaveReferralRequest(
            "",
            ""
        )

        //2- Mock calls
        mainCoroutineRule.runBlockingTest {
            val resEmail = ApiResponse.Success(200, mockVerifyEmailResponse)
            coEvery { customersApi.emailVerification(mockEmailRequest) } returns resEmail
            val resSignup = ApiResponse.Success(200, mockSignupResponse)
            coEvery { customersApi.signUp(mockSignUpRequest) } returns resSignup
            val resDemoGraphics = ApiResponse.Success(200, BaseApiResponse())
            coEvery { customersApi.postDemographicData(mockDemoGraphicsRequest) } returns resDemoGraphics
            val resAccounts = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.getAccountInfo() } returns resAccounts
            val referralResponse = ApiResponse.Success(200, BaseApiResponse())
            coEvery { customersApi.saveReferralInvitation(mockReferralRequest) } returns referralResponse
            Mockito.`when`(sharedPreferenceManager.getReferralInfo())
                .thenReturn(ReferralInfo("1", "121212"))


            //2-Call
            emailVerificationVM =
                EmailVerificationVM(
                    EmailVerificationState(),
                    customersApi,
                    sharedPreferenceManager,
                    SessionManager(customersApi, authApi, sharedPreferenceManager),
                    pkFeatureFlags = mockk()
                )
            emailVerificationVM.verifyEmailAndSignup(
                mockEmailRequest,
                mockSignUpRequest,
                mockDemoGraphicsRequest
            )

            // Then
            assertEquals(true, emailVerificationVM.savProfileEvent.getOrAwaitValue())
        }
    }


    @Test
    fun `test signup user successfully if given email is already existed email `() {
        //Given
        val mockEmailRequest =
            EmailVerificationRequest(
                email = "faheem@digitify.com",
                accountType = AccountType.B2C_ACCOUNT.name,
                token = "213123123123"
            )

        //2- Mock calls
        mainCoroutineRule.runBlockingTest {
            val resEmail = ApiResponse.Error(ApiError(400, "User already exist with this email"))
            coEvery { customersApi.emailVerification(mockEmailRequest) } returns resEmail

            //2-Call
            emailVerificationVM = EmailVerificationVM(
                EmailVerificationState(), customersApi, mockk(),
                mockk(), pkFeatureFlags = mockk()
            )
            emailVerificationVM.verifyEmailAndSignup(
                mockEmailRequest,
                mockk(),
                mockk()
            )

            // Then
            assertEquals(false, emailVerificationVM.savProfileEvent.getOrAwaitValue())
        }
    }

    private fun getEmailMockResponse(fileName: String): BaseResponse<String> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseResponse<String>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }

    private fun getSignupMockResponse(fileName: String): BaseResponse<String> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseResponse<String>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }


    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}