package com.yap.yappk.pk.ui.dashboard.cards.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ForgotCardPINUC @Inject constructor(
    private val cardsRepository: CardsApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<ForgotCardPINUC.RequestValues,
        ForgotCardPINUC.ResponseValue,
        ForgotCardPINUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                cardsRepository.forgotCardPIN(
                    newPin = requestValues?.newPin ?: "",
                    token = requestValues?.token ?: "",
                    cardSerialNumber = requestValues?.cardSerialNumber ?: ""
                )
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

    class RequestValues(val cardSerialNumber: String, val newPin: String, val token: String) :
        BaseUseCase.RequestValues

    class ResponseValue : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}