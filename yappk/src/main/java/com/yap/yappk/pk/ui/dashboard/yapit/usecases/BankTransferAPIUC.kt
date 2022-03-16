package com.yap.yappk.pk.ui.dashboard.yapit.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.AccountBalance
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferLimits
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferReasons
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionFeeResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BankTransferAPIUC @Inject constructor(
    private val transactionsApi: TransactionsApi,
    private val customersApi: CustomersApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<BankTransferAPIUC.RequestValues, BankTransferAPIUC.ResponseValue, BankTransferAPIUC.ErrorValue>() {

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
            val deferredReasonsResponse =
                async { transactionsApi.getTransferReasonsRequest() }
            val feeResponse = deferredFeeResponse.await()
            val fundTransferLimitsResponse = deferredFundTransferLimitsResponse.await()
            val transactionThresholdResponse = deferredTransactionThresholdResponse.await()
            val accountBalanceResponse = deferredAccountBalanceResponse.await()
            val reasonsResponse = deferredReasonsResponse.await()
            withContext(Dispatchers.Main) {
                var transactionFeeResponse: TransactionFeeResponse? = null
                var transactionLimitsResponse: FundTransferLimits? = null
                var thresholdResponse: TransactionThresholdResponse? = null
                var accountBalance: AccountBalance? = null
                var transferReasons: List<FundTransferReasons>? = null
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
                when (reasonsResponse) {
                    is ApiResponse.Success -> {
                        transferReasons = reasonsResponse.data.data
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = reasonsResponse.error.message))
                    }
                }

                responseCallback?.onSuccess(
                    ResponseValue(
                        transactionFeeResponse = transactionFeeResponse,
                        fundTransferLimitsResponse = transactionLimitsResponse,
                        transactionThresholdResponse = thresholdResponse,
                        accountBalanceResponse = accountBalance,
                        reasonsResponse = transferReasons
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
        val reasonsResponse: List<FundTransferReasons>?
    ) : BaseUseCase.ResponseValue

    class ErrorValue(val msg: String) : ResponseError

}