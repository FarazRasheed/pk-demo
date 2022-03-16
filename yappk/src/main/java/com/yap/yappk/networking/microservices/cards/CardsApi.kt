package com.yap.yappk.networking.microservices.cards

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.requestdtos.VerifyCardPinRequest
import com.yap.yappk.networking.microservices.cards.responsedtos.*
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo

interface CardsApi {
    suspend fun getUserDebitCard(): ApiResponse<BaseResponse<Card>>
    suspend fun getUserCards(): ApiResponse<BaseListResponse<Card>>
    suspend fun setCardPin(cardPin: String, cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun getCardDetail(cardSerialNumber: String): ApiResponse<BaseResponse<CardDetail>>
    suspend fun configAllowAtm(cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun configAbroadPayment(cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun configOnlineBanking(cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun configRetailPayment(cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun configFreezeUnfreezeCard(cardSerialNumber: String): ApiResponse<BaseApiResponse>
    suspend fun verifyCardPin(
        cardSerialNumber: String,
        verifyCardPinRequest: VerifyCardPinRequest
    ): ApiResponse<BaseApiResponse>

    suspend fun changeCardPin(
        oldPin: String,
        newPin: String,
        confirmPin: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse>

    suspend fun setCardName(
        cardName: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse>

    suspend fun forgotCardPIN(
        newPin: String,
        token: String,
        cardSerialNumber: String
    ): ApiResponse<BaseApiResponse>

    suspend fun getPhysicalCardAddress(): ApiResponse<BaseResponse<Address>>
    suspend fun getCardBalance(cardSerialNumber: String): ApiResponse<BaseResponse<CardBalance>>
    suspend fun reportAndBlockCard(
        cardSerialNumber: String,
        hotListReason: String
    ): ApiResponse<BaseApiResponse>

    suspend fun reorderDebitCard(address: Address): ApiResponse<BaseApiResponse>
    suspend fun getCardSchemes(): ApiResponse<BaseListResponse<CardScheme>>
    suspend fun getCardSchemeBenefits(cardScheme: String): ApiResponse<BaseListResponse<CardSchemeBenefit>>
    suspend fun saveAddressAndCreateCard(cardOrderRequest: CardOrderRequest): ApiResponse<BaseResponse<AccountInfo>>
    suspend fun orderPhysicalCard(
        cardFee: String,
        cardScheme: String
    ): ApiResponse<BaseResponse<AccountInfo>>


}
