package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.main

import com.digitify.core.base.interfaces.IBase

interface IMainQr {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}