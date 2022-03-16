package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard

import com.digitify.core.base.interfaces.IBase

interface IAddMoneyDashboard {
    interface View : IBase.View<ViewModel> {
        fun openAccountDetailScreen()
        fun openCardScreen()
    }

    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}