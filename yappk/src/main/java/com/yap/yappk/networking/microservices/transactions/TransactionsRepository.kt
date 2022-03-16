package com.yap.yappk.networking.microservices.transactions

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.base.BaseRepository
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.transactions.requestdtos.*
import com.yap.yappk.networking.microservices.transactions.responsedtos.*
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession
import javax.inject.Inject

class TransactionsRepository @Inject constructor(val service: TransactionsRetroService) :
    BaseRepository(), TransactionsApi {

    override suspend fun getMinMaxFilterAmount(): ApiResponse<BaseResponse<FilterAmount>> =
        executeSafely(call = {
            service.getMinMaxFilterAmount()
        })

    override suspend fun getCardTransactions(
        pageNumber: Int?,
        pageSize: Int?,
        cardSerialNumber: String?,
        minAmount: Double?,
        maxAmount: Double?,
        txnType: String?
    ): ApiResponse<BaseResponse<TransactionResponse>> =
        executeSafely(call = {
            service.getCardTransactions(
                pageNumber = pageNumber,
                pageSize = pageSize,
                cardSerialNumber = cardSerialNumber,
                minAmount = minAmount,
                maxAmount = maxAmount,
                txnType = txnType
            )
        })

    override suspend fun getPhysicalCardFee(): ApiResponse<BaseResponse<RemittanceFee>> {
        return executeSafely(call = {
            service.getPhysicalCardFee()
        })
    }

    override suspend fun y2yFundsTransferRequest(
        receiverUUID: String?,
        beneficiaryName: String?,
        deviceId: String?,
        amount: String?,
        otpVerificationReq: Boolean?,
        remarks: String?
    ): ApiResponse<BaseResponse<YapToYapTransaction>> {
        return executeSafely(call = {
            val y2YFundsTransferRequest = Y2YFundsTransferRequest(
                receiverUUID = receiverUUID,
                beneficiaryName = beneficiaryName,
                deviceId = deviceId,
                amount = amount,
                otpVerificationReq = otpVerificationReq,
                remarks = remarks
            )
            service.y2yFundsTransferRequest(y2YFundsTransferRequest)
        })
    }

    override suspend fun getTransactionFeeWithProductCode(productCode: String?): ApiResponse<BaseResponse<TransactionFeeResponse>> {
        return executeSafely(call = {
            service.getTransactionFeeWithProductCode(productCode = productCode)
        })
    }

    override suspend fun getFundTransferLimits(productCode: String?): ApiResponse<BaseResponse<FundTransferLimits>> {
        return executeSafely(call = {
            service.getFundTransferLimits(productCode = productCode)
        })
    }

    override suspend fun getTransactionThresholds(): ApiResponse<BaseResponse<TransactionThresholdResponse>> {
        return executeSafely(call = {
            service.getTransactionThresholds()
        })
    }

    override suspend fun getFundTransferDenominations(productCode: String?): ApiResponse<BaseListResponse<FundTransferDenominations>> {
        return executeSafely(call = {
            service.getFundTransferDenominations(productCode = productCode)
        })
    }

    override suspend fun createTransactionSession(
        sessionId: String?,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<CreateSession>> {
        return executeSafely(call = {
            val session: Session? = if (sessionId.isNullOrEmpty()) null else Session(id = sessionId)
            val createSessionRequest =
                CreateSessionRequest(Order(currency = currency, amount = amount), session = session)
            service.createTransactionSession(createSessionRequest)
        })
    }

    override suspend fun check3DEnrollmentSession(
        beneficiaryId: Int?,
        orderId: String?,
        sessionId: String?,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<Check3DEnrollmentSession>> {
        return executeSafely(call = {
            val check3DEnrollmentSessionRequest = Check3DEnrollmentSessionRequest(
                beneficiaryId = beneficiaryId,
                order = Order(id = orderId, currency = currency, amount = amount),
                Session(id = sessionId)
            )
            service.check3DEnrollmentSession(check3DEnrollmentSessionRequest)
        })
    }

    override suspend fun check3DEnrollmentSessionOnBoarding(
        beneficiaryId: Int?,
        orderId: String?,
        sessionId: String?,
        currency: String?,
        amount: String?
    ): ApiResponse<BaseResponse<Check3DEnrollmentSession>> {
        return executeSafely(call = {
            val session = if (sessionId.isNullOrEmpty()) null else Session(id = sessionId)
            val bnId = if (sessionId.isNullOrEmpty()) beneficiaryId else null
            val check3DEnrollmentSessionRequest = Check3DEnrollmentSessionRequest(
                beneficiaryId = bnId,
                order = Order(id = orderId, currency = currency, amount = amount),
                session = session
            )
            service.check3DEnrollmentSession(check3DEnrollmentSessionRequest)
        })
    }

    override suspend fun secureIdPooling(
        secureId: String?
    ): ApiResponse<BaseResponse<String>> {
        return executeSafely(call = {
            service.secureIdPooling(secureId)
        })
    }

    override suspend fun cardTopUpTransactionRequest(
        beneficiaryId: String,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            service.cardTopUpTransactionRequest(
                orderId,
                TopUpTransactionRequest(
                    securityCode = securityCode,
                    beneficiaryId = beneficiaryId,
                    order = Order(id = orderId, currency = currency, amount = amount), `3DSecureId` = secureId
                )
            )
        })
    }

    override suspend fun onBoardingCardTopUpTransactionRequest(
        beneficiaryId: String?,
        sessionId: String?,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String?
    ): ApiResponse<BaseApiResponse> {
        return executeSafely(call = {
            val session = if (sessionId.isNullOrEmpty()) null else Session(id = sessionId)
            val bnId = if (sessionId.isNullOrEmpty()) beneficiaryId else null
            service.onBoardingCardTopUpTransactionRequest(
                orderId,
                TopUpTransactionRequest(
                    securityCode = securityCode,
                    beneficiaryId = bnId,
                    order = Order(id = orderId, currency = currency, amount = amount), `3DSecureId` = secureId,
                    session = session
                )
            )
        })
    }

    override suspend fun getTransferReasonsRequest(): ApiResponse<BaseListResponse<FundTransferReasons>> {
        return executeSafely(call = {
            service.getTransferReasonsRequest()
        })
    }

    override suspend fun bankTransferRequest(
        beneficiaryId: String?,
        consumerId: String?,
        amount: String?,
        purposeCode: String?,
        purposeReason: String?,
        remarks: String?,
        feeAmount: String?
    ): ApiResponse<BaseResponse<BankTransferResponse>> {
        return executeSafely(call = {
            val bankTransferRequest = BankTransferRequest(
                beneficiaryId = beneficiaryId,
                consumerId = consumerId,
                amount = amount,
                purposeCode = purposeCode,
                purposeReason = purposeReason,
                remarks = remarks,
                feeAmount = feeAmount
            )
            service.bankTransferRequest(bankTransferRequest)
        })
    }

    companion object {
        const val URL_GET_FILTER_MIN_MAX_AMOUNT =
            "/transactions/api/transactions/search-filter/amount"
        const val URL_GET_CARD_TRANSACTIONS =
            "/transactions/api/cards-transactions/{page_number}/{page_size}"
        const val URL_GET_DEBIT_CARD_FEE = "/transactions/api/fees/reorder/debit-card/subscription/physical"
        const val URL_Y2Y_FUNDS_TRANSFER = "/transactions/api/y2y"
        const val URL_GET_TRANSACTION_FEE_WITH_PRODUCT_CODE = "/transactions/api/product-codes/{product-code}/fees"
        const val URL_FUND_TRANSFER_LIMITS = "/transactions/api/product/{product-code}/limits"
        const val URL_GET_TRANSACTION_THRESHOLDS = "/transactions/api/transaction-thresholds"
        const val URL_FUND_TRANSFER_DENOMINATIONS =
            "/transactions/api/product/{product-code}/denominations"
        const val URL_CREATE_TRANSACTION_SESSION =
            "/transactions/api/mastercard/create-checkout-session"
        const val URL_CHECK_3Ds_ENROLLMENT_SESSION = "/transactions/api/mastercard/check-3ds-enrollment"
        const val URL_SECURE_ID_POOLING =
            "/transactions/api/mastercard/retrieve-acs-results/3DSecureId/{secureId}"
        const val URL_TOP_UP_TRANSACTION =
            "/transactions/api/mastercard/order-id/{order-id}"
        const val URL_ON_BOARDING_TOP_UP_TRANSACTION =
            "/transactions/api/mastercard/first-credit/order-id/{order-id}"
        const val URL_GET_TRANSFER_REASONS =
            "/transactions/api/reasons-of-transfer"
        const val URL_BANK_TRANSFER =
            "/transactions/api/bank-transfer"
    }
}