package com.yap.yappk.pk.ui.dashboard.cards.carditem.addcard

import com.digitify.core.base.interfaces.IBase

interface IAddCard {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}