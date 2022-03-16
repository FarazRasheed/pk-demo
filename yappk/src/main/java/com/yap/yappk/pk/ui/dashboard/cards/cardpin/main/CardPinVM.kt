package com.yap.yappk.pk.ui.dashboard.cards.cardpin.main

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPinVM @Inject constructor(
    override val viewState: CardPinState
) : BaseViewModel<ICardPin.State>(), ICardPin.ViewModel {

    override var debitCard: Card? = null
}