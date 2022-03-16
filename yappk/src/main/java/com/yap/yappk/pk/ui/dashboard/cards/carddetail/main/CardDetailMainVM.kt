package com.yap.yappk.pk.ui.dashboard.cards.carddetail.main

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardDetailMainVM @Inject constructor(
    override val viewState: CardDetailMainState
) : BaseViewModel<ICardDetailMain.State>(), ICardDetailMain.ViewModel {
    override var card: Card? = null
    override var cardPosition: Int? = null
}