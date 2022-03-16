package com.yap.yappk.pk.ui.onboarding.webview

import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.SingleClickEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewVM @Inject constructor() : BaseViewModel<IWebView.State>(), IWebView.ViewModel {

    override val viewState: IWebView.State = WebViewState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
}