package com.yap.yappk.pk.ui.kyc.customer.invalid

import com.digitify.core.base.interfaces.IBase

interface IInvalidCustomer {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State
}