package com.yap.yappk.pk.ui.generic.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VerifyOtpUC @Inject constructor(
    private val messagesApi: MessagesApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<VerifyOtpUC.RequestValues,
        VerifyOtpUC.ResponseValue,
        VerifyOtpUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                messagesApi.verifyOtp(
                    action = requestValues?.action ?: "",
                    otp = requestValues?.otp ?: ""
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue(token = response.data.data))
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(val otp: String, val action: String) :
        BaseUseCase.RequestValues

    class ResponseValue(var token: String?) : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}