package com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardDeliveryVM @Inject constructor(
    override val viewState: CardDeliveryState
) : BaseViewModel<ICardDelivery.State>(), ICardDelivery.ViewModel {
    override var debitCard: Card? = null
}