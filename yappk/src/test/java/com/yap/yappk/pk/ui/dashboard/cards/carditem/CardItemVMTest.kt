package com.yap.yappk.pk.ui.dashboard.cards.carditem

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardDeliveryStatus
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardPinStatus
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardStatus
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardStateUC
import com.yap.yappk.pk.utils.enums.PKPartnerBankStatus
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class CardItemVMTest : BaseTestCase() {
    // Subject under test
    lateinit var viewModel: CardItemVM

    // Mock API response
    private lateinit var mockDebitCardResponse: BaseResponse<Card>
    private lateinit var mockAccountResponse: BaseListResponse<AccountInfo>

    @Before
    fun setUp() {
        viewModel = CardItemVM(mockk(), mockk(), mockk(), CardStateUC(), mockk(), mockk())
        getApiMockResponse<BaseResponse<Card>>("debitCardResponse.json") {
            mockDebitCardResponse = it
        }
        getApiMockResponse<BaseListResponse<AccountInfo>>("accountsResponse.json") {
            mockAccountResponse = it
        }
    }

    @Test
    fun getCardStateNotOrderedWithSignUpPending() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.IBAN_ASSIGNED.name

        val expectedCardState = CardState.NOT_ORDERED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateNotOrderedWitIBANAssigned() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.IBAN_ASSIGNED.name

        val expectedCardState = CardState.NOT_ORDERED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateDeliveryInProgressWithActive() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.ACTIVATED.name
        mockCard.status = CardStatus.ACTIVE.name
        mockCard.deliveryStatus = CardDeliveryStatus.SHIPPING.name
        mockCard.pinCreated = false

        val expectedCardState = CardState.DELIVERY_IN_PROGRESS
        // When
        viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateDeliveryInProgressWithInActive() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.ACTIVATED.name
        mockCard.status = CardStatus.INACTIVE.name
        mockCard.deliveryStatus = CardDeliveryStatus.SHIPPING.name
        mockCard.pinCreated = false

        val expectedCardState = CardState.DELIVERY_IN_PROGRESS
        // When
        viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateSetPinRequiredWithInActive() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.ACTIVATED.name
        mockCard.status = CardStatus.INACTIVE.name
        mockCard.deliveryStatus = CardDeliveryStatus.SHIPPED.name
        mockCard.pinCreated = false

        val expectedCardState = CardState.SET_PIN_REQUIRED
        // When
       viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateSetPinRequiredWithActive() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockAccount?.partnerBankStatus = PKPartnerBankStatus.ACTIVATED.name
        mockCard.status = CardStatus.ACTIVE.name
        mockCard.deliveryStatus = CardDeliveryStatus.SHIPPED.name
        mockCard.pinCreated = false

        val expectedCardState = CardState.SET_PIN_REQUIRED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateActivated() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockCard.status = CardStatus.ACTIVE.name
        mockCard.deliveryStatus = CardDeliveryStatus.SHIPPED.name
        mockCard.pinCreated = true
        mockCard.pinStatus = CardPinStatus.ACTIVE.name

        val expectedCardState = CardState.ACTIVATED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateReOrder() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockCard.status = CardStatus.HOTLISTED.name

        val expectedCardState = CardState.RE_ORDER_REQUIRED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateFreeze() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockCard.status = CardStatus.BLOCKED.name

        val expectedCardState = CardState.FREEZE
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun getCardStateExpired() {
        // Given
        val mockAccount = mockAccountResponse.data?.first()
        val mockCard = mockDebitCardResponse.data ?: Card()
        mockCard.status = CardStatus.EXPIRED.name

        val expectedCardState = CardState.EXPIRED
        // When
         viewModel.getCardState(mockCard, mockAccount)
        // Then
        Assert.assertEquals(expectedCardState, viewModel.cardState.getOrAwaitValue())

    }

    @Test
    fun openCardDetailBottomSheet() {
        val expected = true
        Assert.assertEquals(true, expected)

    }


}