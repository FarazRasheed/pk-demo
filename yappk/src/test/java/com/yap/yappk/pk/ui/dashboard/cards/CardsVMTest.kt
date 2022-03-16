package com.yap.yappk.pk.ui.dashboard.cards

import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.localization.screen_card_section_display_text_card_page_count_text
import com.yap.yappk.localization.screen_card_status_display_text_title
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CardsVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var cardsVM: CardsVM

    private val viewState: CardsState = CardsState()

    //Mock ResourcesProvider
    private lateinit var resourcesProviders: ResourcesProviders

    //Session Manager
    private lateinit var sessionManager: SessionManager

    // Mock SharePref
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    private lateinit var authApi: AuthApi

    // Mock API response
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>
    private lateinit var mockCardsResponse: BaseListResponse<Card>
    private lateinit var mockDebitCardResponse: BaseResponse<Card>

    @Before
    fun setUp() {
        getApiMockResponse<BaseListResponse<AccountInfo>>("accountsResponse.json") {
            mockAccountsResponse = it
        }
        getApiMockResponse<BaseListResponse<Card>>("cardsResponse.json") {
            mockCardsResponse = it
        }
        getApiMockResponse<BaseResponse<Card>>("debitCardResponse.json") {
            mockDebitCardResponse = it
        }
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        sharedPreferenceManager = Mockito.mock(SharedPreferenceManager::class.java)
        authApi = Mockito.mock(AuthApi::class.java)
    }

    @Test
    fun `test set card list not empty`() {
        //Given
        val list = mockCardsResponse.data ?: mutableListOf()
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val resAccounts = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.getAccountInfo() } returns resAccounts
            sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)

            //2-Call
            cardsVM = CardsVM(viewState, resourcesProviders, sessionManager, mockk())
            cardsVM.setCardList(list)

            //Verify
            Assert.assertEquals(false, cardsVM.cardList.getOrAwaitValue().isNullOrEmpty())
        }
    }

    @Test
    fun `test get card name of debit type`() {
        //Given
        val card = mockDebitCardResponse.data ?: Card()
        Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
            .thenReturn("Primary Card")
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)

        //2-Call
        cardsVM = CardsVM(viewState, resourcesProviders, sessionManager, mockk())
        val cardName = cardsVM.getCardName(card)

        val expected = resourcesProviders.getString(screen_card_status_display_text_title)

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card name of prepaid type`() {
        //Given
        val card = mockDebitCardResponse.data ?: Card()
        card.cardType = CardType.PREPAID.name
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)

        //2-Call
        cardsVM = CardsVM(viewState, resourcesProviders, sessionManager, mockk())
        val cardName = cardsVM.getCardName(card)

        val expected = card.cardName
        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get title`() {
        //Given
        Mockito.`when`(
            resourcesProviders.getString(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
            )
        )
            .thenReturn("1/1")
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)

        //2-Call
        cardsVM = CardsVM(viewState, resourcesProviders, sessionManager, mockk())
        val countTitle = cardsVM.getPageCountTitle(0, mockCardsResponse.data?.size ?: 0)

        val expected = resourcesProviders.getString(
            screen_card_section_display_text_card_page_count_text,
            "0",
            "${mockCardsResponse.data?.size ?: 0}"
        )

        //Verify
        Assert.assertEquals(expected, countTitle)
    }

    @Test
    fun `test card list sort by type`() {
        //Given
        val cardList: ArrayList<Card> = mockCardsResponse.data as ArrayList<Card>
        val card = mockDebitCardResponse.data ?: Card()
        card.cardType = CardType.PREPAID.name
        cardList.add(card)
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)

        //2-Call
        cardsVM = CardsVM(viewState, resourcesProviders, sessionManager, mockk())
        val sortList = cardsVM.sortByType(cardList)

        //Verify
        Assert.assertEquals(CardType.DEBIT.name, sortList.first().cardType)
        Assert.assertEquals(CardType.PREPAID.name, sortList.last().cardType)
    }
}