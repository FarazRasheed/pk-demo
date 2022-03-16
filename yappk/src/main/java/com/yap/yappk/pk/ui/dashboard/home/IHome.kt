package com.yap.yappk.pk.ui.dashboard.home

import com.digitify.core.base.interfaces.IBase

interface IHome {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun logout(accountUUID: String, success: () -> Unit)
    }

    interface State : IBase.State

}