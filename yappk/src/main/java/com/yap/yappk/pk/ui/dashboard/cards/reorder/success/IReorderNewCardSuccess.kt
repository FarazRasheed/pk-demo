package com.yap.yappk.pk.ui.dashboard.cards.reorder.success

import com.digitify.core.base.interfaces.IBase

interface IReorderNewCardSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}