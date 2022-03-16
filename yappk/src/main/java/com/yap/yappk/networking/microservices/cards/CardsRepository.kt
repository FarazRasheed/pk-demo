package com.yap.yappk.networking.microservices.cards

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.base.BaseRepository
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.requestdtos.*
import com.yap.yappk.networking.microservices.cards.responsedtos.*
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val service: CardsRetroService
) : BaseRepository(), CardsApi {

    override suspend fun getUserDebitCard(): ApiResponse<BaseResponse<Card>> =
        executeSafely(call = { service.getUserDebitCard() })

    override suspend fun getUserCards(): ApiResponse<BaseListResponse<Card>> =
        executeSafely(call = { service.getUserCards() })

    override suspend fun setCardPin(
        cardPin: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse> {

        val cardPinRequest = CardPinRequest(cardPin = cardPin)
        return executeSafely(call = { service.setCardPin(cardSerialNumber, cardPinRequest) })
    }

    override suspend fun getCardDetail(cardSerialNumber: String): ApiResponse<BaseResponse<CardDetail>> =
        executeSafely(call = { service.getCardDetail(cardSerialNumber) })

    override suspend fun configAllowAtm(cardSerialNumber: String): ApiResponse<BaseApiResponse> {
        val cardLimitConfigRequest = CardLimitConfigRequest(cardSerialNumber = cardSerialNumber)
        return executeSafely(call = { service.configAllowAtm(cardLimitConfigRequest) })
    }

    override suspend fun configAbroadPayment(cardSerialNumber: String): ApiResponse<BaseApiResponse> {
        val cardLimitConfigRequest = CardLimitConfigRequest(cardSerialNumber = cardSerialNumber)
        return executeSafely(call = { service.configAbroadPayment(cardLimitConfigRequest) })
    }

    override suspend fun configOnlineBanking(cardSerialNumber: String): ApiResponse<BaseApiResponse> {
        val cardLimitConfigRequest = CardLimitConfigRequest(cardSerialNumber = cardSerialNumber)
        return executeSafely(call = { service.configOnlineBanking(cardLimitConfigRequest) })
    }

    override suspend fun configRetailPayment(cardSerialNumber: String): ApiResponse<BaseApiResponse> {
        val cardLimitConfigRequest = CardLimitConfigRequest(cardSerialNumber = cardSerialNumber)
        return executeSafely(call = { service.configRetailPayment(cardLimitConfigRequest) })
    }

    override suspend fun configFreezeUnfreezeCard(cardSerialNumber: String): ApiResponse<BaseApiResponse> {
        val cardLimitConfigRequest = CardLimitConfigRequest(cardSerialNumber = cardSerialNumber)
        return executeSafely(call = { service.configFreezeUnfreezeCard(cardLimitConfigRequest) })
    }

    override suspend fun verifyCardPin(
        cardSerialNumber: String,
        verifyCardPinRequest: VerifyCardPinRequest
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            service.verifyCardPin(cardSerialNumber, verifyCardPinRequest)
        })
    }

    override suspend fun changeCardPin(
        oldPin: String,
        newPin: String,
        confirmPin: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            service.changeCardPin(
                ChangeCardPinRequest(oldPin, newPin, confirmPin, cardSerialNumber)
            )
        })
    }

    override suspend fun setCardName(
        cardName: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = { service.setCardName(cardName, cardSerialNumber) })
    }

    override suspend fun forgotCardPIN(
        newPin: String,
        token: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse> {
        val request = ForgotCardPINRequest(newPin = newPin, token = token)
        return executeSafely(call = {
            service.forgotCardPIN(
                cardSerialNumber = cardSerialNumber,
                forgotCardPINRequest = request
            )
        })
    }

    override suspend fun getPhysicalCardAddress(): ApiResponse<BaseResponse<Address>> {
        return executeSafely(call = {
            service.getPhysicalCardAddress()
        })
    }

    override suspend fun getCardBalance(cardSerialNumber: String): ApiResponse<BaseResponse<CardBalance>> {
        return executeSafely(call = {
            service.getCardBalance(cardSerialNumber)
        })
    }

    override suspend fun reportAndBlockCard(
        cardSerialNumber: String,
        hotListReason: String
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            val cardsHotListRequest =
                CardsHotListRequest(
                    cardSerialNumber = cardSerialNumber,
                    hotListReason = hotListReason
                )
            service.reportAndBlockCard(cardsHotListRequest)
        })
    }

    override suspend fun reorderDebitCard(address: Address): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            service.reorderDebitCard(address)
        })
    }

    override suspend fun getCardSchemes(): ApiResponse<BaseListResponse<CardScheme>> {
        return executeSafely(call = {
            service.getCardSchemes()
        })
    }

    override suspend fun getCardSchemeBenefits(cardScheme: String): ApiResponse<BaseListResponse<CardSchemeBenefit>> {
        return executeSafely(call = {
            service.getCardSchemeBenefits(scheme = cardScheme)
        })
    }

    override suspend fun saveAddressAndCreateCard(cardOrderRequest: CardOrderRequest): ApiResponse<BaseResponse<AccountInfo>> {
        return executeSafely(call = {
            service.saveAddressAndCreateCard(cardOrderRequest = cardOrderRequest)
        })
    }

    override suspend fun orderPhysicalCard(
        cardFee: String,
        cardScheme: String
    ): ApiResponse<BaseResponse<AccountInfo>> {
        return executeSafely(call = {
            service.orderPhysicalCard(
                cardOrderRequest = OrderCardRequest(
                    cardFee = cardFee,
                    cardScheme = cardScheme
                )
            )
        })
    }

    companion object {
        const val URL_GET_DEBIT_CARD = "/cards/api/cards/debit"
        const val URL_GET_CARDS = "/cards/api/cards"
        const val URL_SET_PIN = "/cards/api/cards/create-pin/{card_serial_number}"
        const val URL_GET_CARD_DETAIL = "/cards/api/cards/details"
        const val URL_ALLOW_ATM = "/cards/api/cards/atm-allow"
        const val URL_ONLINE_BANKING = "/cards/api/cards/online-banking"
        const val URL_ABROAD_PAYMENT = "/cards/api/cards/payment-abroad"
        const val URL_RETAIL_PAYMENT = "/cards/api/cards/retail-payment"
        const val URL_CARD_FREEZE_UNFREEZE = "/cards/api/cards/block-unblock"
        const val URL_VERIFY_CARD_PIN = "/cards/api/cards/verify-pin/{card_serial_number}"
        const val URL_CHANGE_CARD_PIN = "/cards/api/cards/change-pin"
        const val URL_SET_CARD_NAME = "cards/api/cards/card-name"
        const val URL_FORGOT_CARD_PIN = "/cards/api/cards/forgot-pin/{card_serial_number}"
        const val URL_GET_PHYSICAL_CARD_ADDRESS = "/cards/api/user-address"
        const val URL_GET_CARD_BALANCE = "/cards/api/cards/balance"
        const val URL_REPORT_LOST_OR_STOLEN_CARD = "/cards/api/card-hot-list"
        const val URL_REORDER_DEBIT_CARD = "/cards/api/cards/debit/reorder"
        const val URL_GET_CARD_SCHEME = "/cards/api/schemes/active"
        const val URL_GET_CARD_SCHEME_BENEFITS = "/cards/api/scheme-benefits/active/{scheme}"
        const val URL_SAVE_ADDRESS_CREATE_CARD_HOLDER =
            "cards/api/save-address-and-create-cardholder"
        const val URL_CREATE_CARD_HOLDER = "cards/api/order-physical-card-of-cardholder"

    }

}
