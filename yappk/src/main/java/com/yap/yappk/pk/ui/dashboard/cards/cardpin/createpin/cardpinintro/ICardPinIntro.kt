package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinintro

import com.digitify.core.base.interfaces.IBase

interface ICardPinIntro {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}