package com.yap.yappk.pk.ui.dashboard.yapit.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.AccountBalance
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferDenominations
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferLimits
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionFeeResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FundsTransferAPIUC @Inject constructor(
    private val transactionsApi: TransactionsApi,
    private val customersApi: CustomersApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<FundsTransferAPIUC.RequestValues, FundsTransferAPIUC.ResponseValue, FundsTransferAPIUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val deferredFeeResponse = async {
                transactionsApi.getTransactionFeeWithProductCode(
                    requestValues?.transactionProductCode
                )
            }
            val deferredFundTransferLimitsResponse =
                async { transactionsApi.getFundTransferLimits(requestValues?.transactionProductCode) }
            val deferredTransactionThresholdResponse = async { transactionsApi.getTransactionThresholds() }
            val deferredAccountBalanceResponse = async {
                customersApi.getAccountBalance()
            }
            val deferredDenominationResponse =
                async { transactionsApi.getFundTransferDenominations(requestValues?.transactionProductCode) }
            val feeResponse = deferredFeeResponse.await()
            val fundTransferLimitsResponse = deferredFundTransferLimitsResponse.await()
            val transactionThresholdResponse = deferredTransactionThresholdResponse.await()
            val accountBalanceResponse = deferredAccountBalanceResponse.await()
            val denominationResponse = deferredDenominationResponse.await()
            withContext(Dispatchers.Main) {
                var transactionFeeResponse: TransactionFeeResponse? = null
                var transactionLimitsResponse: FundTransferLimits? = null
                var thresholdResponse: TransactionThresholdResponse? = null
                var accountBalance: AccountBalance? = null
                var transferDenominations: List<FundTransferDenominations>? = null
                when (feeResponse) {
                    is ApiResponse.Success -> {
                        transactionFeeResponse = feeResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = feeResponse.error.message))
                    }
                }
                when (fundTransferLimitsResponse) {
                    is ApiResponse.Success -> {
                        transactionLimitsResponse = fundTransferLimitsResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = fundTransferLimitsResponse.error.message))
                    }
                }

                when (transactionThresholdResponse) {
                    is ApiResponse.Success -> {
                        thresholdResponse = transactionThresholdResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = transactionThresholdResponse.error.message))
                    }
                }

                when (accountBalanceResponse) {
                    is ApiResponse.Success -> {
                        accountBalance = accountBalanceResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = accountBalanceResponse.error.message))
                    }
                }
                when (denominationResponse) {
                    is ApiResponse.Success -> {
                        transferDenominations = denominationResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = denominationResponse.error.message))
                    }
                }

                responseCallback?.onSuccess(
                    ResponseValue(
                        transactionFeeResponse = transactionFeeResponse,
                        fundTransferLimitsResponse = transactionLimitsResponse,
                        transactionThresholdResponse = thresholdResponse,
                        accountBalanceResponse = accountBalance,
                        denominationsResponse = transferDenominations
                    )
                )
            }

        }
    }

    class RequestValues(val transactionProductCode: String) : BaseUseCase.RequestValues
    class ResponseValue(
        val transactionFeeResponse: TransactionFeeResponse?,
        val fundTransferLimitsResponse: FundTransferLimits?,
        val transactionThresholdResponse: TransactionThresholdResponse?,
        val accountBalanceResponse: AccountBalance?,
        val denominationsResponse: List<FundTransferDenominations>?
    ) : BaseUseCase.ResponseValue

    class ErrorValue(val msg: String) : ResponseError

}