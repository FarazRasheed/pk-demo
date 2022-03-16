package com.yap.yappk.pk.ui.kyc.secretquestions.birthcity

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.VerifySecretQuestionsRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlaceOfBirthVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var placeOfBirthVM: PlaceOfBirthVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    lateinit var sessionManager: SessionManager

    private val viewState: PlaceOfBirthState = PlaceOfBirthState()


    @Before
    fun setUp() {
        sessionManager = SessionManager(customersApi, mockk(), mockk())
        placeOfBirthVM = PlaceOfBirthVM(viewState, customersApi, sessionManager)
    }

    @Test
    fun `Test get secret question city of birth should return list`() {
        val mockAnswers = arrayListOf("abc", "ali", "alice")
        val mockRes = BaseListResponse<String>()
        mockRes.data = mockAnswers

        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Success(200, mockRes)
        coEvery { customersApi.getPlaceOfBirthCities() } returns mockDocumentResponse

        //When
        placeOfBirthVM.getCityOfBirthNames()

        // verify results
        Assert.assertEquals(
            false,
            placeOfBirthVM.cityOfBirthNamesList.getOrAwaitValue().isNullOrEmpty()
        )
    }

    @Test
    fun `Test get secret question city of birth should return 400`() {
        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Error(ApiError(400, "Error"))
        coEvery { customersApi.getPlaceOfBirthCities() } returns mockDocumentResponse

        //When
        placeOfBirthVM.getCityOfBirthNames()

        // verify results
        Assert.assertEquals(
            true,
            placeOfBirthVM.cityOfBirthNamesList.getOrAwaitValue().isNullOrEmpty()
        )
    }

    @Test
    fun `Test verify secret question answers should return 200`() {
        // Given
        val mockRequest =
            VerifySecretQuestionsRequest(motherMaidenName = "abc", cityOfBirth = "lahore")

        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Success(200, BaseResponse<Boolean>())
        coEvery { customersApi.verifySecretQuestions(mockRequest) } returns mockDocumentResponse
        val resAccounts = ApiResponse.Success(200, getAccountsMockResponse())
        coEvery { customersApi.getAccountInfo() } returns resAccounts

        //When
        placeOfBirthVM.verifyQuestionAnswers(mockRequest)

        // verify results
        Assert.assertEquals(
            true,
            placeOfBirthVM.isVerified.getOrAwaitValue()
        )
    }

    @Test
    fun `Test verify secret question answers should return 400`() {
        // Given
        val mockRequest =
            VerifySecretQuestionsRequest(motherMaidenName = "abc", cityOfBirth = "lahore")

        // Mock calls
        val mockResponse =
            ApiResponse.Error(ApiError(400, "Error"))
        coEvery { customersApi.verifySecretQuestions(mockRequest) } returns mockResponse
        val resAccounts = ApiResponse.Success(200, getAccountsMockResponse())
        coEvery { customersApi.getAccountInfo() } returns resAccounts

        //When
        placeOfBirthVM.verifyQuestionAnswers(mockRequest)

        // verify results
        Assert.assertEquals(
            false,
            placeOfBirthVM.isVerified.getOrAwaitValue()
        )
    }

    private fun getAccountsMockResponse(): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(
            ReadAssetFile.readFileFromTestResources("accountsResponse.json"),
            itemType
        )
    }
}