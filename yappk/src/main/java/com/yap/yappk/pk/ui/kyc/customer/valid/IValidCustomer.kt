package com.yap.yappk.pk.ui.kyc.customer.valid

import com.digitify.core.base.interfaces.IBase

interface IValidCustomer {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State

}
