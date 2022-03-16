package com.yap.yappk.pk.ui.kyc.selfiereview

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
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.kyc.selfie.selfiereview.SelfieReviewState
import com.yap.yappk.pk.ui.kyc.selfie.selfiereview.SelfieReviewVM
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class SelfieReviewVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var selfieReviewVM: SelfieReviewVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private lateinit var authApi: AuthApi

    private val viewState: SelfieReviewState = SelfieReviewState()

    // Mock API response
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
    }

    @Test
    fun `test selfie uploaded api success`() {
        //Given
        val imagePath = ""
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(
                200,
                BaseApiResponse()
            )
            coEvery { customersApi.uploadSelfie(imagePath) } returns response
            val resAccounts = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.getAccountInfo() } returns resAccounts

            //2-Call
            selfieReviewVM = SelfieReviewVM(viewState, customersApi, sessionManager)
            selfieReviewVM.uploadSelfie(imagePath)

            //Verify
            Assert.assertEquals(true, selfieReviewVM.isSelfieUploaded.getOrAwaitValue())
        }
    }

    @Test
    fun `test selfie uploaded api error`() {
        //Given
        val imagePath = ""
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(
                ApiError(400, "Error")
            )
            coEvery { customersApi.uploadSelfie(imagePath) } returns response
            //2-Call
            selfieReviewVM = SelfieReviewVM(viewState, customersApi, sessionManager)
            selfieReviewVM = SelfieReviewVM(viewState, customersApi, sessionManager)
            selfieReviewVM.uploadSelfie(imagePath)

            //Verify
            Assert.assertEquals(false, selfieReviewVM.isSelfieUploaded.getOrAwaitValue())
        }
    }

    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}