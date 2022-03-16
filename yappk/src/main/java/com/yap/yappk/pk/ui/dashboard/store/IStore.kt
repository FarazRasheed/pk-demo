package com.yap.yappk.pk.ui.dashboard.store

import com.digitify.core.base.interfaces.IBase

interface IStore {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}