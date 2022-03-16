package com.yap.yappk.networking.microservices.transactions

import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.transactions.requestdtos.BankTransferRequest
import com.yap.yappk.networking.microservices.transactions.requestdtos.Check3DEnrollmentSessionRequest
import com.yap.yappk.networking.microservices.transactions.requestdtos.CreateSessionRequest
import com.yap.yappk.networking.microservices.transactions.requestdtos.Y2YFundsTransferRequest
import com.yap.yappk.networking.microservices.transactions.responsedtos.*
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession
import retrofit2.Response
import retrofit2.http.*

interface TransactionsRetroService {

    // Get min max filter amount
    @GET(TransactionsRepository.URL_GET_FILTER_MIN_MAX_AMOUNT)
    suspend fun getMinMaxFilterAmount(): Response<BaseResponse<FilterAmount>>

    @GET(TransactionsRepository.URL_GET_CARD_TRANSACTIONS)
    suspend fun getCardTransactions(
        @Path("page_number") pageNumber: Int?,
        @Path("page_size") pageSize: Int?,
        @Query("cardSerialNumber") cardSerialNumber: String?,
        @Query("amountStartRange") minAmount: Double?,
        @Query("amountEndRange") maxAmount: Double?,
        @Query("txnType") txnType: String?,
    ): Response<BaseResponse<TransactionResponse>>

    @GET(TransactionsRepository.URL_GET_DEBIT_CARD_FEE)
    suspend fun getPhysicalCardFee(): Response<BaseResponse<RemittanceFee>>

    @POST(TransactionsRepository.URL_Y2Y_FUNDS_TRANSFER)
    suspend fun y2yFundsTransferRequest(@Body y2YFundsTransferRequest: Y2YFundsTransferRequest?): Response<BaseResponse<YapToYapTransaction>>

    @POST(TransactionsRepository.URL_GET_TRANSACTION_FEE_WITH_PRODUCT_CODE)
    suspend fun getTransactionFeeWithProductCode(@Path("product-code") productCode: String?): Response<BaseResponse<TransactionFeeResponse>>

    @GET(TransactionsRepository.URL_FUND_TRANSFER_LIMITS)
    suspend fun getFundTransferLimits(@Path("product-code") productCode: String?): Response<BaseResponse<FundTransferLimits>>

    @GET(TransactionsRepository.URL_GET_TRANSACTION_THRESHOLDS)
    suspend fun getTransactionThresholds(): Response<BaseResponse<TransactionThresholdResponse>>

    @GET(TransactionsRepository.URL_FUND_TRANSFER_DENOMINATIONS)
    suspend fun getFundTransferDenominations(@Path("product-code") productCode: String?): Response<BaseListResponse<FundTransferDenominations>>

    @POST(TransactionsRepository.URL_CREATE_TRANSACTION_SESSION)
    suspend fun createTransactionSession(@Body createSessionRequest: CreateSessionRequest?): Response<BaseResponse<CreateSession>>

    @PUT(TransactionsRepository.URL_CHECK_3Ds_ENROLLMENT_SESSION)
    suspend fun check3DEnrollmentSession(@Body check3DEnrollmentSessionRequest: Check3DEnrollmentSessionRequest?): Response<BaseResponse<Check3DEnrollmentSession>>

    @GET(TransactionsRepository.URL_SECURE_ID_POOLING)
    suspend fun secureIdPooling(
        @Path("secureId") secureId: String?
    ): Response<BaseResponse<String>>

    @PUT(TransactionsRepository.URL_TOP_UP_TRANSACTION)
    suspend fun cardTopUpTransactionRequest(
        @Path("order-id") orderId: String,
        @Body topUpTransactionRequest: TopUpTransactionRequest
    ): Response<BaseApiResponse>

    @PUT(TransactionsRepository.URL_ON_BOARDING_TOP_UP_TRANSACTION)
    suspend fun onBoardingCardTopUpTransactionRequest(
        @Path("order-id") orderId: String,
        @Body topUpTransactionRequest: TopUpTransactionRequest
    ): Response<BaseApiResponse>

    @GET(TransactionsRepository.URL_GET_TRANSFER_REASONS)
    suspend fun getTransferReasonsRequest(
    ): Response<BaseListResponse<FundTransferReasons>>

    @POST(TransactionsRepository.URL_BANK_TRANSFER)
    suspend fun bankTransferRequest(@Body bankTransferRequest: BankTransferRequest): Response<BaseResponse<BankTransferResponse>>

}