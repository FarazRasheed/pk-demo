package com.yap.yappk.networking.microservices.transactions

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.*
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession

interface TransactionsApi {
    suspend fun getMinMaxFilterAmount(): ApiResponse<BaseResponse<FilterAmount>>
    suspend fun getCardTransactions(
        pageNumber: Int?,
        pageSize: Int?,
        cardSerialNumber: String?,
        minAmount: Double?,
        maxAmount: Double?,
        txnType: String?,
    ): ApiResponse<BaseResponse<TransactionResponse>>

    suspend fun getPhysicalCardFee(): ApiResponse<BaseResponse<RemittanceFee>>
    suspend fun y2yFundsTransferRequest(
        receiverUUID: String?,
        beneficiaryName: String?,
        deviceId: String?,
        amount: String?,
        otpVerificationReq: Boolean?,
        remarks: String?
    ): ApiResponse<BaseResponse<YapToYapTransaction>>

    suspend fun getTransactionFeeWithProductCode(productCode: String?): ApiResponse<BaseResponse<TransactionFeeResponse>>
    suspend fun getFundTransferLimits(productCode: String?): ApiResponse<BaseResponse<FundTransferLimits>>
    suspend fun getTransactionThresholds(): ApiResponse<BaseResponse<TransactionThresholdResponse>>
    suspend fun getFundTransferDenominations(productCode: String?): ApiResponse<BaseListResponse<FundTransferDenominations>>
    suspend fun createTransactionSession(
        sessionId: String? = null,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<CreateSession>>

    suspend fun check3DEnrollmentSessionOnBoarding(
        beneficiaryId: Int?,
        orderId: String?,
        sessionId: String?,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<Check3DEnrollmentSession>>

    suspend fun check3DEnrollmentSession(
        beneficiaryId: Int?,
        orderId: String?,
        sessionId: String?,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<Check3DEnrollmentSession>>

    suspend fun secureIdPooling(
        secureId: String?
    ): ApiResponse<BaseResponse<String>>

    suspend fun cardTopUpTransactionRequest(
        beneficiaryId: String,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String
    ): ApiResponse<BaseApiResponse>

    suspend fun onBoardingCardTopUpTransactionRequest(
        beneficiaryId: String?,
        sessionId: String?,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String?
    ): ApiResponse<BaseApiResponse>

    suspend fun getTransferReasonsRequest(): ApiResponse<BaseListResponse<FundTransferReasons>>

    suspend fun bankTransferRequest(
        beneficiaryId: String?,
        consumerId: String?,
        amount: String?,
        purposeCode: String?,
        purposeReason: String?,
        remarks: String?,
        feeAmount: String?
    ): ApiResponse<BaseResponse<BankTransferResponse>>

}