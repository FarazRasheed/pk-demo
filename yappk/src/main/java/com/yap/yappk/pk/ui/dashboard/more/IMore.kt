package com.yap.yappk.pk.ui.dashboard.more

import com.digitify.core.base.interfaces.IBase

interface IMore {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State
}