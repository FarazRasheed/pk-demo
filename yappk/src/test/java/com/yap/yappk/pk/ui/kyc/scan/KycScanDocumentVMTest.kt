package com.yap.yappk.pk.ui.kyc.scan

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.DocumentDetailRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.DocumentDetails
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.Documents
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.uploaddocument.scan.KycScanDocumentState
import com.yap.yappk.pk.ui.kyc.uploaddocument.scan.KycScanDocumentVM
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class KycScanDocumentVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var kycScanDocumentVM: KycScanDocumentVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private val sessionManager: SessionManager = Mockito.mock(SessionManager::class.java)
    private val viewState: KycScanDocumentState = KycScanDocumentState()

    @Before
    fun init() {
        kycScanDocumentVM = KycScanDocumentVM(viewState, sessionManager, customersApi)
    }

    @Test
    fun `Test get document details from NADRA if given details are correct`() {
        // Given
        val mockRequest =
            DocumentDetailRequest(cnic = "3520285704903", dateOfIssuance = "2020-10-01")

        // Mock calls
        val mockResponse =
            ApiResponse.Success(
                200,
                generateMockDocumentDataResponse("documentDataNadraResponse.json")
            )
        coEvery { customersApi.getDocumentDetail(mockRequest) } returns mockResponse

        //When
        kycScanDocumentVM.getDocumentDetails(mockRequest)

        // verify results
        Assert.assertNotNull(kycScanDocumentVM.documentDetails.getOrAwaitValue())
    }

    @Test
    fun `Test get document details from NADRA return 400`() {
        // Given
        val mockRequest =
            DocumentDetailRequest(cnic = "3520285704903", dateOfIssuance = "2020-10-01")

        // Mock calls
        val mockResponse =
            ApiResponse.Error(
                ApiError(400, "Error")
            )
        coEvery { customersApi.getDocumentDetail(mockRequest) } returns mockResponse

        //When
        kycScanDocumentVM.getDocumentDetails(mockRequest)

        // verify results
        Assert.assertNull(kycScanDocumentVM.documentDetails.getOrAwaitValue())
    }

    @Test
    fun `Test if given citizen number is correct and correctly formatted`() {
        // Given
        val citizenNumber = "3520285704903"
        val expected = "35202-8570490-3"

        // Then
        val actual = kycScanDocumentVM.getFormattedCitizenNumber(citizenNumber)

        // Verify
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Test if given citizen number is incorrect and result is empty`() {
        // Given
        val citizenNumber = "352028570490"
        val expected = ""

        // Then
        val actual = kycScanDocumentVM.getFormattedCitizenNumber(citizenNumber)

        // Verify
        Assert.assertEquals(expected, actual)
    }


    private fun generateMockDocumentDataResponse(fileName: String): BaseResponse<DocumentDetails> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseResponse<Documents>>() {}.type
        return gson.fromJson(
            ReadAssetFile.readFileFromTestResources(fileName),
            itemType
        )
    }

}