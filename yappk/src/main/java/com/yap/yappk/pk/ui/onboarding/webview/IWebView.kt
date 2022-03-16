package com.yap.yappk.pk.ui.onboarding.webview

import com.digitify.core.base.interfaces.IBase

interface IWebView {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State {
        var pageUrl: String?
    }
}