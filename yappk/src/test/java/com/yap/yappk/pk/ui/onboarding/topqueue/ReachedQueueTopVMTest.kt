package com.yap.yappk.pk.ui.onboarding.topqueue

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
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CompleteVerificationRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ReachedQueueTopVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var reachedQueueTopVM: ReachedQueueTopVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()
    private val authApi: AuthApi = mockk()

    private val viewState: ReachedQueueTopState = ReachedQueueTopState()

    // Mock SharePref
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private lateinit var sessionManager: SessionManager

    // Mock API response
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>

    @Before
    fun setUp() {
        mockAccountsResponse = getAccountsMockResponse("accountsResponse.json")
        sharedPreferenceManager = Mockito.mock(SharedPreferenceManager::class.java)
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)
    }

    @Test
    fun `test verification complete api success`() {
        val mobileNo = "3224642870"
        val countryCode = "0092"
        var request = CompleteVerificationRequest(
            countryCode = countryCode,
            mobileNo = mobileNo
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(
                200,
                BaseApiResponse()
            )
            val accountsResponse = ApiResponse.Success(200, mockAccountsResponse)
            coEvery {
                customersApi.completeVerification(request)
            } returns response
            coEvery { customersApi.getAccountInfo() } returns accountsResponse
            //2-Call
            reachedQueueTopVM = ReachedQueueTopVM(viewState, sessionManager, customersApi)
            reachedQueueTopVM.completeVerification(request)
            //Verify
            Assert.assertEquals(true, reachedQueueTopVM.isCompleteVerification.getOrAwaitValue())
        }
    }

    @Test
    fun `test verification complete api error`() {
        val mobileNo = "3224642870"
        val countryCode = "0092"
        var request = CompleteVerificationRequest(
            countryCode = countryCode,
            mobileNo = mobileNo
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(
                ApiError(400, "")
            )
            coEvery { customersApi.completeVerification(request) } returns response
            //2-Call
            reachedQueueTopVM = ReachedQueueTopVM(viewState, sessionManager, customersApi)
            reachedQueueTopVM.completeVerification(request)
            //Verify
            Assert.assertEquals(false, reachedQueueTopVM.isCompleteVerification.getOrAwaitValue())
        }
    }

    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}