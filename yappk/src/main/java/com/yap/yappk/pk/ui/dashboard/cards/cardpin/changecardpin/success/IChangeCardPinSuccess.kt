package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.success

import com.digitify.core.base.interfaces.IBase

interface IChangeCardPinSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}