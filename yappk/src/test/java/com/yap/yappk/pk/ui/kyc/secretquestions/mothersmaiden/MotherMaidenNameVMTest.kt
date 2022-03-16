package com.yap.yappk.pk.ui.kyc.secretquestions.mothersmaiden

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MotherMaidenNameVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var motherMaidenNameVM: MotherMaidenNameVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private val viewState: MothersMaidenNameState = MothersMaidenNameState()


    @Before
    fun setUp() {
        motherMaidenNameVM = MotherMaidenNameVM(viewState, customersApi)
    }

    @Test
    fun `Test get secret question mother name should return list`() {
        val mockAnswers = arrayListOf("abc", "ali", "alice")
        val mockRes = BaseListResponse<String>()
        mockRes.data = mockAnswers

        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Success(200, mockRes)
        coEvery { customersApi.getMotherMaidenNames() } returns mockDocumentResponse

        //When
        motherMaidenNameVM.getMotherMaidenNames()

        // verify results
        Assert.assertEquals(
            false,
            motherMaidenNameVM.motherNamesList.getOrAwaitValue().isNullOrEmpty()
        )
    }

    @Test
    fun `Test get secret question mother name should return 400`() {
        // Mock calls
        val mockDocumentResponse =
            ApiResponse.Error(ApiError(400, "Error"))
        coEvery { customersApi.getMotherMaidenNames() } returns mockDocumentResponse

        //When
        motherMaidenNameVM.getMotherMaidenNames()

        // verify results
        Assert.assertEquals(
            true,
            motherMaidenNameVM.motherNamesList.getOrAwaitValue().isNullOrEmpty()
        )
    }
}