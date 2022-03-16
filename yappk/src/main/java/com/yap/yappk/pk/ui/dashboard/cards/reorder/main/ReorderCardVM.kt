package com.yap.yappk.pk.ui.dashboard.cards.reorder.main

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReorderCardVM @Inject constructor(
    override val viewState: ReorderCardState
) :
    BaseViewModel<IReorderCard.State>(), IReorderCard.ViewModel {
    override var card: Card? = null
}