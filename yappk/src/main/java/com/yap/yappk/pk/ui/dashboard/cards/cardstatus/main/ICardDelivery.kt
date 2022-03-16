package com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main

import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface ICardDelivery {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var debitCard: Card?
    }

    interface State : IBase.State
}