package com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase

interface UseCaseCallback<R, E> {
    fun onSuccess(response: R)
    fun onError(error: E)
}
