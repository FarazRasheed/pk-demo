package com.yap.yappk.pk.ui.kyc.selfie.takeselfie

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase

interface ITakeSelfie {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State {
        val hasFace: MutableLiveData<Boolean>
    }
}