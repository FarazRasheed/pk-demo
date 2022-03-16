package com.yap.yappk.pk.ui.onboarding.passcode

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase

interface IPassCode {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State {
        var passcode: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var passcodeError: MutableLiveData<String>
    }
}