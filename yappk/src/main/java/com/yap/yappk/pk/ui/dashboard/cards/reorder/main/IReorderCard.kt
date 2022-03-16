package com.yap.yappk.pk.ui.dashboard.cards.reorder.main

import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface IReorderCard {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var card: Card?
    }

    interface State : IBase.State
}