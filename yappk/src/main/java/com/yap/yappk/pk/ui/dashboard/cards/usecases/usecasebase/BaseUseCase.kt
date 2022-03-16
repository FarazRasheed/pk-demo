package com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase

abstract class BaseUseCase<Q : BaseUseCase.RequestValues, P : BaseUseCase.ResponseValue, L : BaseUseCase.ResponseError> {

//    var requestValues: Q? = null
//
//    var useCaseCallback: UseCaseCallback<P, L>? = null
//
//    internal suspend fun run() {
//        executeUseCase(requestValues, useCaseCallback)
//    }

    abstract fun executeUseCase(
        requestValues: Q?,
        responseCallback: UseCaseCallback<P, L>?
    )

    /**
     * Data passed to a request.
     */
    interface RequestValues

    /**
     * Data received from a request.
     */
    interface ResponseValue

    /**
     * Error received from a request.
     */
    interface ResponseError

}