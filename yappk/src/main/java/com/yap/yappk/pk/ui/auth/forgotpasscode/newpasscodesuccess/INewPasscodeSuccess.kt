package com.yap.yappk.pk.ui.auth.forgotpasscode.newpasscodesuccess

import com.digitify.core.base.interfaces.IBase

interface INewPasscodeSuccess {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State

}