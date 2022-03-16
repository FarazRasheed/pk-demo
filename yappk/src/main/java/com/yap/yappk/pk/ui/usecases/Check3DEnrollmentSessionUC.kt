package com.yap.yappk.pk.ui.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class Check3DEnrollmentSessionUC @Inject constructor(
    private val transactionsApi: TransactionsApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<Check3DEnrollmentSessionUC.RequestValues,
        Check3DEnrollmentSessionUC.ResponseValue,
        Check3DEnrollmentSessionUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                transactionsApi.check3DEnrollmentSessionOnBoarding(
                    beneficiaryId = requestValues?.beneficiaryId,
                    orderId = requestValues?.orderId,
                    sessionId = requestValues?.sessionId,
                    currency = requestValues?.currency,
                    amount = requestValues?.amount
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue(response.data.data))
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(
        val beneficiaryId: Int?,
        val orderId: String?,
        val sessionId: String?,
        val currency: String?,
        val amount: String?
    ) : BaseUseCase.RequestValues

    class ResponseValue(val check3dEnrollmentResponse: Check3DEnrollmentSession?) :
        BaseUseCase.ResponseValue

    class ErrorValue(val msg: String) : ResponseError
}