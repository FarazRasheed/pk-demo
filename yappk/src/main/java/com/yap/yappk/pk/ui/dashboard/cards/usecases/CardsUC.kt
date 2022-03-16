package com.yap.yappk.pk.ui.dashboard.cards.usecases

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CardsUC @Inject constructor(
    private val cardsRepository: CardsApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<CardsUC.RequestValues,
        CardsUC.ResponseValue,
        CardsUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                cardsRepository.getUserCards()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue(cards = response.data.data))
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues : BaseUseCase.RequestValues
    class ResponseValue(val cards: List<Card>?) : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}