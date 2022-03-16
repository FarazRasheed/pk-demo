package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.scanqr

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.SessionManager

interface IScanQr {
    interface View : IBase.View<ViewModel>{
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val userInfo: LiveData<IBeneficiary?>
        fun getQrCodeUserInfo(qrCode: String)
    }

    interface State : IBase.State
}