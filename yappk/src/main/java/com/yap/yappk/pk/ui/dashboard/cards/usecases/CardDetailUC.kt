package com.yap.yappk.pk.ui.dashboard.cards.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CardDetailUC @Inject constructor(
    private val cardsRepository: CardsApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<CardDetailUC.RequestValues,
        CardDetailUC.ResponseValue,
        CardDetailUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                cardsRepository.getCardDetail(requestValues?.cardSerialNumber ?: "")
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

    class RequestValues(val cardSerialNumber: String) : BaseUseCase.RequestValues
    class ResponseValue(val cardDetail: CardDetail?) : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}