package com.yap.yappk.pk.ui.dashboard.cards.carddetail.main

import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface ICardDetailMain {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var card: Card?
        var cardPosition: Int?
    }

    interface State : IBase.State
}