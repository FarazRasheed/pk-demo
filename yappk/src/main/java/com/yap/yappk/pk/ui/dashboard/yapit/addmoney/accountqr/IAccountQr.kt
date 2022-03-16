package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountqr

import com.digitify.core.base.interfaces.IBase

interface IAccountQr {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}