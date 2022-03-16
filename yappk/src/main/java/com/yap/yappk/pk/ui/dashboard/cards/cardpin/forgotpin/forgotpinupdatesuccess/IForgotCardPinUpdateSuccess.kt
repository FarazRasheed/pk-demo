package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.forgotpinupdatesuccess

import com.digitify.core.base.interfaces.IBase

interface IForgotCardPinUpdateSuccess {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State
}