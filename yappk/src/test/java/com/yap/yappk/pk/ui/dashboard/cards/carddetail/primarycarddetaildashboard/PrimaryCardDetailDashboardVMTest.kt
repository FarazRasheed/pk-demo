package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard

import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.localization.screen_card_detail_display_text_Unfreeze
import com.yap.yappk.localization.screen_card_detail_display_text_freeze
import com.yap.yappk.localization.screen_card_status_display_text_title
import com.yap.yappk.localization.screen_card_status_display_text_virtual
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionResponse
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardSettings
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.enums.TransactionType
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardDetailUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardFreezeUnfreezeUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardTransactionUC
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class PrimaryCardDetailDashboardVMTest : BaseTestCase() {
    // Subject under test
    lateinit var viewModel: PrimaryCardDetailDashboardVM

    // Use a fake UseCase to be injected into the viewModel
    private val cardApi: CardsApi = mockk()
    private val transactionsApi: TransactionsApi = mockk()

    @Before
    fun setUp() {

    }

    @Test
    fun `freeze unfreeze card config change success`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configFreezeUnfreezeCard(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Success<BaseApiResponse>>()


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(relaxed = true),
            mockk(),
            mockk(),
            CardFreezeUnfreezeUC(cardApi, mainCoroutineRule.coroutineContext),
            mockk(),
            mockk()
        )
        viewModel.updateCardFreezeConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(true, viewModel.isFreezeCardConfigUpdated.getOrAwaitValue())
    }

    @Test
    fun `freeze unfreeze card config change error`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configFreezeUnfreezeCard(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Error>(
            relaxed = true
        )

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(relaxed = true),
            mockk(),
            mockk(),
            CardFreezeUnfreezeUC(cardApi, mainCoroutineRule.coroutineContext),
            mockk(),
            mockk()
        )
        viewModel.updateCardFreezeConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(false, viewModel.isFreezeCardConfigUpdated.getOrAwaitValue())
    }


    @Test
    fun `get card details api null response success`() {
        //Given
        val cardSerialNo = "Absjag"
        val response = ApiResponse.Success(200, mockk<BaseResponse<CardDetail>> {
            every { data } returns null
        })

        //1- Mock calls
        coEvery { cardApi.getCardDetail(cardSerialNumber = cardSerialNo) } returns response


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            CardDetailUC(cardApi, mainCoroutineRule.coroutineContext),
            mockk()
        )
        viewModel.fetchCardDetails(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(StateEnum.EMPTY, viewModel.cardDetails.getOrAwaitValue().state)
    }


    @Test
    fun `get card details api success`() {
        //Given
        val cardSerialNo = "Absjag"
        val response = ApiResponse.Success(200, mockk<BaseResponse<CardDetail>> {
            every { data } returns CardDetail()
        })

        //1- Mock calls
        coEvery { cardApi.getCardDetail(cardSerialNumber = cardSerialNo) } returns response


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            CardDetailUC(cardApi, mainCoroutineRule.coroutineContext),
            mockk()
        )
        viewModel.fetchCardDetails(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(StateEnum.CONTENT, viewModel.cardDetails.value?.state)
    }

    @Test
    fun `get card details api Error`() {
        //Given
        val cardSerialNo = "Absjag"

        //1- Mock calls
        coEvery { cardApi.getCardDetail(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Error>(
            relaxed = true
        )


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(relaxed = true),
            mockk(),
            mockk(),
            mockk(),
            CardDetailUC(cardApi, mainCoroutineRule.coroutineContext),
            mockk()
        )
        viewModel.fetchCardDetails(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(StateEnum.ERROR, viewModel.cardDetails.value?.state)
    }

    @Test
    fun `test get card name with debit type`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        val card = mockk<Card>(relaxed = true) {
            every { cardType } returns CardType.DEBIT.name
        }
        every { resourcesProviders.getString(screen_card_status_display_text_title) } returns ""

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardName(card)
        val expected = resourcesProviders.getString(screen_card_status_display_text_title)

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card name with prepaid type`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        val card = mockk<Card>(relaxed = true) {
            every { cardType } returns CardType.PREPAID.name
        }
        every { card.cardName } returns ""

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardName(card)
        val expected = card.cardName

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card type text with prepaid type`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        val card = mockk<Card>(relaxed = true) {
            every { cardType } returns CardType.PREPAID.name
        }
        every { resourcesProviders.getString(screen_card_status_display_text_virtual) } returns "Card Type Prepaid"

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardTypeText(card)
        val expected = resourcesProviders.getString(screen_card_status_display_text_virtual)

        //Verify
        Assert.assertEquals(expected, cardName)
    }


    @Test
    fun `test get card type text with debit type`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        val card = mockk<Card>(relaxed = true) {
            every { cardType } returns CardType.DEBIT.name
        }
        every { resourcesProviders.getString(screen_card_status_display_text_title) } returns "Card Type Debit"

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardTypeText(card)
        val expected = resourcesProviders.getString(screen_card_status_display_text_title)

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card freeze text`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        every { resourcesProviders.getString(screen_card_detail_display_text_freeze) } returns "Freeze"

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardFreezeText(false)
        val expected = resourcesProviders.getString(screen_card_detail_display_text_freeze)

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card unfreeze text`() {
        //Given
        val resourcesProviders: ResourcesProviders = mockk(relaxed = true)
        every { resourcesProviders.getString(screen_card_detail_display_text_Unfreeze) } returns "UnFreeze"

        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            resourcesProviders,
            mockk(),
            mockk(),
            mock(),
            mockk()
        )
        val cardName = viewModel.getCardFreezeText(true)
        val expected = resourcesProviders.getString(screen_card_detail_display_text_Unfreeze)

        //Verify
        Assert.assertEquals(expected, cardName)
    }

    @Test
    fun `test get card balance`() {
        //Given
        val sessionManager: SessionManager = mockk(relaxed = true)
        val card = mockk<Card>(relaxed = true)
        every { sessionManager.userAccount.value?.currency?.code } returns "PKR"
        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            mockk(),
            sessionManager,
            mockk(),
            mock(),
            mockk()
        )
        val balance = viewModel.getCardBalance(card)
        val expected = "PKR 0.0"

        //Verify
        Assert.assertEquals(expected, balance)
    }

    @Test
    fun `test get target screen`() {
        //Given
        val card = mockk<Card>()
        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )

        val screen = viewModel.getTargetScreen(0, card)
        val expected = CardSettings.ChangeCardName(card)

        //Verify
        Assert.assertEquals(expected, screen)
    }

    @Test
    fun `get card transaction api response success`() {
        //Given
        val pageNumber: Int = 1
        val pageSize: Int = 10
        val cardSerialNumber: String = "Absjag"
        val extra: FilterExtras = FilterExtras(
            txnType = TransactionType.CREDIT.name,
            minRange = "0",
            maxRange = "2500",
        )
        val response = ApiResponse.Success(200, mockk<BaseResponse<TransactionResponse>> {
            every { data } returns spyk()
        })

        //1- Mock calls
        coEvery {
            transactionsApi.getCardTransactions(
                cardSerialNumber = cardSerialNumber,
                pageNumber = pageNumber,
                pageSize = pageSize,
                minAmount = extra.minRange?.toDouble(),
                maxAmount = extra.maxRange?.toDouble(),
                txnType = extra.txnType
            )
        } returns response


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(relaxed = true),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            CardTransactionUC(transactionsApi, mainCoroutineRule.coroutineContext)
        )
        viewModel.fetchCardTransactions(
            cardSerialNumber = cardSerialNumber,
            pageNumber = pageNumber,
            pageSize = pageSize,
            filterExtras = extra
        )

        //3-verify
        Assert.assertEquals(true, viewModel.isTransactionsSuccess.value)
    }

    @Test
    fun `get card transaction api response error`() {
        //Given
        val pageNumber: Int = 1
        val pageSize: Int = 10
        val cardSerialNumber: String = "Absjag"
        val extra: FilterExtras = FilterExtras(
            txnType = TransactionType.CREDIT.name,
            minRange = "0",
            maxRange = "2500",
        )
        val response =  mockk<ApiResponse.Error>(
            relaxed = true
        )

        //1- Mock calls
        coEvery {
            transactionsApi.getCardTransactions(
                cardSerialNumber = cardSerialNumber,
                pageNumber = pageNumber,
                pageSize = pageSize,
                minAmount = extra.minRange?.toDouble(),
                maxAmount = extra.maxRange?.toDouble(),
                txnType = extra.txnType
            )
        } returns response


        //2-Call
        viewModel = PrimaryCardDetailDashboardVM(
            mockk(relaxed = true),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            CardTransactionUC(transactionsApi, mainCoroutineRule.coroutineContext)
        )
        viewModel.fetchCardTransactions(
            cardSerialNumber = cardSerialNumber,
            pageNumber = pageNumber,
            pageSize = pageSize,
            filterExtras = extra
        )

        //3-verify
        Assert.assertEquals(false, viewModel.isTransactionsSuccess.value)
    }

    @After
    fun cleanUp() {
        clearAllMocks()
    }

}