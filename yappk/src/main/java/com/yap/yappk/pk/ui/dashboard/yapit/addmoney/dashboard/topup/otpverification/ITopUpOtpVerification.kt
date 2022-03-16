package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.otpverification

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase

interface ITopUpOtpVerification {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val htmlUrl: LiveData<String>
        fun setUrl(url: String?)
    }

    interface State : IBase.State
}