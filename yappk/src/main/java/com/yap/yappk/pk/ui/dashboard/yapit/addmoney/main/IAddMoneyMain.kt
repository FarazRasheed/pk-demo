package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main

import com.digitify.core.base.interfaces.IBase

interface IAddMoneyMain {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}