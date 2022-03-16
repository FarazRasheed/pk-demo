package com.yap.yappk.pk.ui.dashboard.cards.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CardTransactionUC @Inject constructor(
    private val transactionsApi: TransactionsApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<CardTransactionUC.RequestValues,
        CardTransactionUC.ResponseValue,
        CardTransactionUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                transactionsApi.getCardTransactions(
                    pageNumber = requestValues?.pageNumber,
                    pageSize = requestValues?.pageSize,
                    cardSerialNumber = requestValues?.cardSerialNumber,
                    minAmount = requestValues?.minRange,
                    maxAmount = requestValues?.maxRange,
                    txnType = requestValues?.txnTyp
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(
                            ResponseValue(
                                transactionList = response.data.data?.content,
                                isLast = response.data.data?.last
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(
        val cardSerialNumber: String,
        val pageSize: Int,
        val pageNumber: Int,
        val minRange: Double?,
        val maxRange: Double?,
        val txnTyp: String?
    ) : BaseUseCase.RequestValues

    class ResponseValue(val transactionList: List<Transaction>?, val isLast: Boolean?) :
        BaseUseCase.ResponseValue

    class ErrorValue(val msg: String) : ResponseError
}