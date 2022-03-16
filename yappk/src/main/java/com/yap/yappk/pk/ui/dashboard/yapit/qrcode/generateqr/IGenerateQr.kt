package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.generateqr

import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.SessionManager

interface IGenerateQr {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
    }

    interface State : IBase.State
}