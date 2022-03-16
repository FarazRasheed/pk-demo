package com.yap.yappk.pk.ui.dashboard.cards

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardsUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CardsUseCaseTest : BaseTestCase() {
    // Subject under test
    private lateinit var cardsUseCase: CardsUC

    // Use a fake UseCase to be injected into the useCase
    private val cardsApi: CardsApi = mockk()

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
    }

    @Test
    fun `test get cards api success`() {

        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            //Given
            cardsUseCase = CardsUC(cardsApi, this.coroutineContext)
            val response = ApiResponse.Success(200, mockCardsResponse)
            coEvery { cardsApi.getUserCards() } returns response
            var cards: List<Card>? = null
            //2-Call
            cardsUseCase.executeUseCase(
                CardsUC.RequestValues(),
                object : UseCaseCallback<CardsUC.ResponseValue, CardsUC.ErrorValue> {
                    override fun onSuccess(response: CardsUC.ResponseValue) {
                        cards = response.cards
                    }

                    override fun onError(error: CardsUC.ErrorValue) {

                    }
                })

            //Verify
            Assert.assertEquals(true, !cards.isNullOrEmpty())
        }
    }

//    @Test
//    fun `test get debit card api success`() {
//        //1- Mock calls
//        mainCoroutineRule.runBlockingTest {
//            //Given
//            cardsUseCase = CardsUseCase(cardsApi, this.coroutineContext)
//            val debitResponse = ApiResponse.Success(200, mockDebitCardResponse)
//            coEvery { cardsApi.getUserDebitCard() } returns debitResponse
//
//            //2-Call
//            val card = cardsUseCase.getDebitCard()
//
//            //Verify
//            Assert.assertEquals(false, card == null)
//        }
//    }

//    @Test
//    fun `test get debit card api error`() {
//        //1- Mock calls
//        mainCoroutineRule.runBlockingTest {
//            //Given
//            cardsUseCase = CardsUseCase(cardsApi, this.coroutineContext)
//            val debitResponse = ApiResponse.Error(ApiError(400, "Error"))
//            coEvery { cardsApi.getUserDebitCard() } returns debitResponse
//
//            //2-Call
//            val card = cardsUseCase.getDebitCard()
//
//            //Verify
//            Assert.assertEquals(true, card == null)
//        }
//    }


    @Test
    fun `test get cards api error`() {
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            //Given
            cardsUseCase = CardsUC(cardsApi, this.coroutineContext)
            val debitResponse = ApiResponse.Success(200, mockDebitCardResponse)
            coEvery { cardsApi.getUserDebitCard() } returns debitResponse
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { cardsApi.getUserCards() } returns response

            //2-Call
            var cards: List<Card>? = null
            //2-Call
            cardsUseCase.executeUseCase(
                CardsUC.RequestValues(),
                object : UseCaseCallback<CardsUC.ResponseValue, CardsUC.ErrorValue> {
                    override fun onSuccess(response: CardsUC.ResponseValue) {
                        cards = response.cards
                    }

                    override fun onError(error: CardsUC.ErrorValue) {

                    }
                })

            //Verify
            Assert.assertEquals(true, cards.isNullOrEmpty())
        }
    }


}