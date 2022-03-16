package com.yap.yappk.networking.microservices.cards

import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.cards.requestdtos.*
import com.yap.yappk.networking.microservices.cards.responsedtos.*
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import retrofit2.Response
import retrofit2.http.*

interface CardsRetroService {
    // Get user debit card only
    @GET(CardsRepository.URL_GET_DEBIT_CARD)
    suspend fun getUserDebitCard(): Response<BaseResponse<Card>>

    // Get user all added cards
    @GET(CardsRepository.URL_GET_CARDS)
    suspend fun getUserCards(): Response<BaseListResponse<Card>>

    // Set card pin
    @POST(CardsRepository.URL_SET_PIN)
    suspend fun setCardPin(
        @Path("card_serial_number") cardSerialNumber: String,
        @Body cardPinRequest: CardPinRequest
    ): Response<BaseApiResponse>

    // Get Card Short details
    @GET(CardsRepository.URL_GET_CARD_DETAIL)
    suspend fun getCardDetail(
        @Query("cardSerialNumber") cardSerialNumber: String
    ): Response<BaseResponse<CardDetail>>

    @PUT(CardsRepository.URL_ALLOW_ATM)
    suspend fun configAllowAtm(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<BaseApiResponse>

    @PUT(CardsRepository.URL_ABROAD_PAYMENT)
    suspend fun configAbroadPayment(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<BaseApiResponse>

    @PUT(CardsRepository.URL_ONLINE_BANKING)
    suspend fun configOnlineBanking(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<BaseApiResponse>

    @PUT(CardsRepository.URL_RETAIL_PAYMENT)
    suspend fun configRetailPayment(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<BaseApiResponse>

    @PUT(CardsRepository.URL_CARD_FREEZE_UNFREEZE)
    suspend fun configFreezeUnfreezeCard(@Body cardLimitConfigRequest: CardLimitConfigRequest): Response<BaseApiResponse>

    //verify card pin
    @POST(CardsRepository.URL_VERIFY_CARD_PIN)
    suspend fun verifyCardPin(
        @Path("card_serial_number") cardSerialNumber: String,
        @Body verifyCardPinRequest: VerifyCardPinRequest
    ): Response<BaseApiResponse>

    //verify card pin
    @POST(CardsRepository.URL_CHANGE_CARD_PIN)
    suspend fun changeCardPin(
        @Body changeCardPinRequest: ChangeCardPinRequest
    ): Response<BaseApiResponse>

    //set card name
    @PUT(CardsRepository.URL_SET_CARD_NAME)
    suspend fun setCardName(
        @Query("cardName") cardName: String, @Query("cardSerialNumber") cardSerialNumber: String
    ): Response<BaseApiResponse>

    //Forgot card pin
    @POST(CardsRepository.URL_FORGOT_CARD_PIN)
    suspend fun forgotCardPIN(
        @Path("card_serial_number") cardSerialNumber: String,
        @Body forgotCardPINRequest: ForgotCardPINRequest
    ): Response<BaseApiResponse>

    //get physical card address
    @GET(CardsRepository.URL_GET_PHYSICAL_CARD_ADDRESS)
    suspend fun getPhysicalCardAddress(
    ): Response<BaseResponse<Address>>

    //get card balance
    @GET(CardsRepository.URL_GET_CARD_BALANCE)
    suspend fun getCardBalance(@Query("cardSerialNumber") cardSerialNumber: String): Response<BaseResponse<CardBalance>>

    //get card balance
    @POST(CardsRepository.URL_REPORT_LOST_OR_STOLEN_CARD)
    suspend fun reportAndBlockCard(
        @Body cardsHotListRequest: CardsHotListRequest
    ): Response<BaseApiResponse>

    //reorder debit card
    @POST(CardsRepository.URL_REORDER_DEBIT_CARD)
    suspend fun reorderDebitCard(@Body address: Address): Response<BaseApiResponse>

    // Get card scheme
    @GET(CardsRepository.URL_GET_CARD_SCHEME)
    suspend fun getCardSchemes(): Response<BaseListResponse<CardScheme>>

    // Get card scheme benefits
    @GET(CardsRepository.URL_GET_CARD_SCHEME_BENEFITS)
    suspend fun getCardSchemeBenefits(
        @Path("scheme") scheme: String,
    ): Response<BaseListResponse<CardSchemeBenefit>>

    // Save user address and order card
    @POST(CardsRepository.URL_SAVE_ADDRESS_CREATE_CARD_HOLDER)
    suspend fun saveAddressAndCreateCard(@Body cardOrderRequest: CardOrderRequest): Response<BaseResponse<AccountInfo>>

    // Create card
    @POST(CardsRepository.URL_CREATE_CARD_HOLDER)
    suspend fun orderPhysicalCard(@Body cardOrderRequest: OrderCardRequest): Response<BaseResponse<AccountInfo>>
}