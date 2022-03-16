package com.yap.yappk.pk.ui.kyc.selfie.selfieguide

import com.digitify.core.base.interfaces.IBase

interface ISelfieGuide {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State

}