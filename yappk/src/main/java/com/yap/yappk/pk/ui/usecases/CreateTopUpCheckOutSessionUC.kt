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

class CreateTopUpCheckOutSessionUC @Inject constructor(
    private val transactionsApi: TransactionsApi,
    private val ioDispatcher: CoroutineContext,
    private val check3dEnrollmentAPIUC: Check3DEnrollmentSessionUC
) : BaseUseCase<CreateTopUpCheckOutSessionUC.RequestValues,
        CreateTopUpCheckOutSessionUC.ResponseValue,
        CreateTopUpCheckOutSessionUC.ErrorValue>() {
    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val createSessionResponse =
                transactionsApi.createTransactionSession(
                    currency = requestValues?.currency,
                    amount = requestValues?.amount,
                    sessionId = requestValues?.sessionId
                )
            withContext(Dispatchers.Main) {
                when (createSessionResponse) {
                    is ApiResponse.Success -> {
                        val bId =
                            if (requestValues?.sessionId.isNullOrEmpty()) requestValues?.beneficiaryId else null
                        check3dEnrollmentAPIUC.executeUseCase(Check3DEnrollmentSessionUC.RequestValues(
                            beneficiaryId = bId,
                            orderId = createSessionResponse.data.data?.order?.id,
                            sessionId = requestValues?.sessionId,
                            currency = requestValues?.currency,
                            amount = requestValues?.amount
                        ),
                            object :
                                UseCaseCallback<Check3DEnrollmentSessionUC.ResponseValue, Check3DEnrollmentSessionUC.ErrorValue> {
                                override fun onSuccess(response: Check3DEnrollmentSessionUC.ResponseValue) {
                                    responseCallback?.onSuccess(
                                        ResponseValue(
                                            createSessionResponse.data.data?.order?.id,
                                            response.check3dEnrollmentResponse
                                        )
                                    )
                                }

                                override fun onError(error: Check3DEnrollmentSessionUC.ErrorValue) {
                                    responseCallback?.onError(ErrorValue(msg = error.msg))
                                }
                            })
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = createSessionResponse.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(
        val currency: String?,
        val amount: String?,
        val beneficiaryId: Int? = null,
        val sessionId: String? = null
    ) :
        BaseUseCase.RequestValues

    class ResponseValue(
        val orderId: String?, val check3dEnrollment: Check3DEnrollmentSession?
    ) : BaseUseCase.ResponseValue

    class ErrorValue(val msg: String) : ResponseError
}