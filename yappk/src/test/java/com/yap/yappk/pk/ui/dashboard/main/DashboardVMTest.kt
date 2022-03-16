package com.yap.yappk.pk.ui.dashboard.main

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardsUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.DebitCardUC
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DashboardVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var dashboardVM: DashboardVM
    private lateinit var cardsUseCase: CardsUC
    private lateinit var debitCardUseCase: DebitCardUC

    // Use a fake UseCase to be injected into the viewModel
    private val cardsApi: CardsApi = mockk()

    // Mock resource provider
    private lateinit var resourcesProviders: ResourcesProviders

    // view state added
    private val viewState = DashboardState()

    // Mock API response
    private lateinit var mockCardsResponse: BaseListResponse<Card>
    private lateinit var mockDebitCardResponse: BaseResponse<Card>

    @Before
    fun setUp() {
        getApiMockResponse<BaseListResponse<Card>>("cardsResponse.json") {
            mockCardsResponse = it
        }
        getApiMockResponse<BaseResponse<Card>>("debitCardResponse.json") {
            mockDebitCardResponse = it
        }
        mainCoroutineRule.runBlockingTest {
            cardsUseCase = CardsUC(cardsApi, this.coroutineContext)
        }
        mainCoroutineRule.runBlockingTest {
            debitCardUseCase = DebitCardUC(cardsApi, this.coroutineContext)
        }
    }

    @Test
    fun `test get cards api success`() {
        //Given
        dashboardVM = DashboardVM(viewState, cardsUseCase, debitCardUseCase, mockk())
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {

            val response = ApiResponse.Success(200, mockCardsResponse)
            coEvery { cardsApi.getUserCards() } returns response

            //2-Call
            dashboardVM.fetchCards()

            //Verify
            Assert.assertEquals(false, dashboardVM.userCards.getOrAwaitValue().isNullOrEmpty())
        }
    }

    @Test
    fun `test get debit card api success`() {
        //Given
        dashboardVM = DashboardVM(viewState, cardsUseCase, debitCardUseCase, mockk())
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {

            val response = ApiResponse.Success(200, mockDebitCardResponse)
            coEvery { cardsApi.getUserDebitCard() } returns response

            //2-Call
            dashboardVM.fetchDebitCard()

            //Verify
            Assert.assertEquals(false, dashboardVM.debitCard.getOrAwaitValue() == null)
        }
    }

    @Test
    fun `test get cards api error`() {
        //Given
        dashboardVM = DashboardVM(viewState, cardsUseCase, debitCardUseCase, mockk())
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {

            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { cardsApi.getUserCards() } returns response

            //2-Call
            dashboardVM.fetchCards()

            //Verify
            Assert.assertEquals(true, dashboardVM.userCards.getOrAwaitValue().isNullOrEmpty())
        }
    }

    @Test
    fun `test get debit card api error`() {
        //Given
        dashboardVM = DashboardVM(viewState, cardsUseCase, debitCardUseCase, mockk())
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {

            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { cardsApi.getUserDebitCard() } returns response

            //2-Call
            dashboardVM.fetchDebitCard()

            //Verify
            Assert.assertEquals(true, dashboardVM.debitCard.getOrAwaitValue() == null)
        }
    }

}