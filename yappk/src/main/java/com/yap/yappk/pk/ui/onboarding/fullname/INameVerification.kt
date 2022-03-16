package com.yap.yappk.pk.ui.onboarding.fullname

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase

interface INameVerification {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State {
        var firstName: MutableLiveData<String>
        var lastName: MutableLiveData<String>
        var isValid: MutableLiveData<Boolean>
    }
}