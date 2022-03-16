package com.yap.yappk.pk.ui.kyc.reviewdetails

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.UploadDocumentsRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails.DocumentReviewState
import com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails.DocumentReviewVM
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class DocumentReviewVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var documentReviewVM: DocumentReviewVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private val viewState: DocumentReviewState = DocumentReviewState()

    lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        sessionManager = SessionManager(customersApi, mockk(), mockk())
        documentReviewVM = DocumentReviewVM(viewState, customersApi, sessionManager)
    }

    @Test
    fun `Test upload document if given details are correct return 200`() {
        // Given
        val mockRequest =
            UploadDocumentsRequest(
                documentType = "CNIC",
                fullName = "Abc xyz",
                identityNo = "123451234564432",
                nationality = "PAK",
                gender = "M",
                dateExpiry = Date(),
                dob = Date(),
                dateIssue = Date(),
                filePaths = arrayListOf()
            )

        // Mock calls
        val mockResponse = ApiResponse.Success(200, BaseApiResponse())
        coEvery { customersApi.uploadDocument(mockRequest) } returns mockResponse
        val resAccounts = ApiResponse.Success(200, getAccountsMockResponse("accountsResponse.json"))
        coEvery { customersApi.getAccountInfo() } returns resAccounts

        //When
        documentReviewVM.uploadDocument(mockRequest)

        // verify results
        Assert.assertEquals(true, documentReviewVM.documentUploaded.getOrAwaitValue())
    }

    @Test
    fun `Test upload document return 400`() {
        // Given
        val mockRequest =
            UploadDocumentsRequest(
                documentType = "CNIC",
                fullName = "Abc xyz",
                identityNo = "123451234564432",
                nationality = "PAK",
                gender = "M",
                dateExpiry = Date(),
                dob = Date(),
                dateIssue = Date(),
                filePaths = arrayListOf()
            )

        // Mock calls
        val mockResponse = ApiResponse.Error(ApiError(400, "Error"))
        coEvery { customersApi.uploadDocument(mockRequest) } returns mockResponse
        val resAccounts = ApiResponse.Success(200, getAccountsMockResponse("accountsResponse.json"))
        coEvery { customersApi.getAccountInfo() } returns resAccounts

        //When
        documentReviewVM.uploadDocument(mockRequest)

        // verify results
        Assert.assertEquals(false, documentReviewVM.documentUploaded.getOrAwaitValue())
    }

    @Test
    fun `Test if given citizen number is correct and correctly formatted`() {
        // Given
        val citizenNumber = "3520285704903"
        val expected = "35202-8570490-3"

        // Then
        val actual = documentReviewVM.getFormattedCitizenNumber(citizenNumber)

        // Verify
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Test if given citizen number is incorrect and result is empty`() {
        // Given
        val citizenNumber = "352028570490"
        val expected = ""

        // Then
        val actual = documentReviewVM.getFormattedCitizenNumber(citizenNumber)

        // Verify
        Assert.assertEquals(expected, actual)
    }


    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}