package com.yap.yappk.pk.ui.kyc.address.success

import com.digitify.core.base.interfaces.IBase

interface ICardOrderSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}