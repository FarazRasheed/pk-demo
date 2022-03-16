package com.yap.yappk.pk.ui.kyc.upload

import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.Documents
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.enums.DocScanStatus
import com.yap.yappk.pk.ui.kyc.uploaddocument.upload.KycUploadDocumentVM
import com.yap.yappk.pk.ui.kyc.uploaddocument.upload.KycUploadSDocumentState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class KycUploadDocumentVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var kycUploadDocumentVM: KycUploadDocumentVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()
    private val sessionManager: SessionManager = mock(SessionManager::class.java)
    private val viewState: KycUploadSDocumentState = KycUploadSDocumentState()


    @Before
    fun init() {
        kycUploadDocumentVM = KycUploadDocumentVM(viewState, sessionManager, customersApi)
    }

    @Test
    fun `Test get document if provided document type is CNIC should return CNIC document`() {
        // Given
        val documentType = "CNIC"

        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Success(200, generateMockDocumentResponse("getDocumentByTypeResponse.json"))
        coEvery { customersApi.getDocumentsByType(documentType) } returns mockDocumentResponse

        //When
        kycUploadDocumentVM.getDocumentBy(DocumentType.CNIC)

        // verify results
        Assert.assertEquals(
            DocScanStatus.DOCS_UPLOADED,
            kycUploadDocumentVM.viewState.documentScanStatus.getOrAwaitValue()
        )
    }

    @Test
    fun `Test get document if provided document type is CNIC should return null`() {
        // Given
        val documentType = "CNIC"

        //Mock calls
        val mockDocumentResponse =
            ApiResponse.Success(200, generateMockDocumentResponse("baseApiResponse.json"))
        coEvery { customersApi.getDocumentsByType(documentType) } returns mockDocumentResponse

        //when
        kycUploadDocumentVM.getDocumentBy(DocumentType.CNIC)

        //verifyResults
        Assert.assertEquals(
            DocScanStatus.SCAN_PENDING,
            kycUploadDocumentVM.viewState.documentScanStatus.getOrAwaitValue()
        )

    }

    @Test
    fun `Test get document if provided document type is CNIC should return error`() {
        // Given
        val documentType = "CNIC"

        //Mock calls
        val mockDocumentResponse =
            ApiResponse.Error(ApiError(400, "Error occurred please contact support"))
        coEvery { customersApi.getDocumentsByType(documentType) } returns mockDocumentResponse

        //when
        kycUploadDocumentVM.getDocumentBy(DocumentType.CNIC)

        //verifyResults
        Assert.assertEquals(
            DocScanStatus.NONE,
            kycUploadDocumentVM.viewState.documentScanStatus.getOrAwaitValue()
        )
    }

    private fun generateMockDocumentResponse(fileName: String): BaseResponse<Documents> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseResponse<Documents>>() {}.type
        return gson.fromJson(
            ReadAssetFile.readFileFromTestResources(fileName),
            itemType
        )
    }
}