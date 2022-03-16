package com.yap.yappk.pk.ui.dashboard.cards.usecases

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

class CreateOtpUC @Inject constructor(
    private val messagesApi: MessagesApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<CreateOtpUC.RequestValues,
        CreateOtpUC.ResponseValue,
        CreateOtpUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                messagesApi.createOtp(requestValues?.action ?: "")
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue())
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(val action: String) : BaseUseCase.RequestValues
    class ResponseValue : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}