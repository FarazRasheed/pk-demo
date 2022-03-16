package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinsuccess

import com.digitify.core.base.interfaces.IBase

interface ICardPinSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}