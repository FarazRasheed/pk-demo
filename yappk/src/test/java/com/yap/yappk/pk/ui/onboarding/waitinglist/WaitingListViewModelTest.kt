package com.yap.yappk.pk.ui.onboarding.waitinglist

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.WaitingListResponse
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
class WaitingListViewModelTest : BaseTestCase() {

    // Subject under test
    private lateinit var waitingListViewModel: WaitingListViewModel

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private val viewState: WaitingListState = WaitingListState()

    // Mock API response
    private lateinit var waitingListResponse: BaseResponse<WaitingListResponse>

    private lateinit var resources: ResourcesProviders

    @Before
    fun setUp() {
        resources = Mockito.mock(ResourcesProviders::class.java)
        waitingListResponse = getMockResponse("waitingListResponse.json")
    }

    @Test
    fun `get waiting list success`() {
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(
                200,
                waitingListResponse
            )
            coEvery { customersApi.getWaitingRanking() } returns response

            //2-Call
            waitingListViewModel = WaitingListViewModel(viewState, customersApi, resources)
            waitingListViewModel.getWaitingRankingList()

            waitingListViewModel.setDataValues(waitingListViewModel.rankingResponse.getOrAwaitValue())

            //3-verify
            Assert.assertEquals(
                response.data.data,
                waitingListViewModel.rankingResponse.getOrAwaitValue()
            )
            Assert.assertEquals(false, waitingListViewModel.viewState.rankList.isNullOrEmpty())
        }
    }

    private fun getMockResponse(fileName: String): BaseResponse<WaitingListResponse> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseResponse<WaitingListResponse>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}