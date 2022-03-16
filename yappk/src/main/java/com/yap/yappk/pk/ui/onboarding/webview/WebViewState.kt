package com.yap.yappk.pk.ui.onboarding.webview

import com.digitify.core.base.BaseState
import javax.inject.Inject

class WebViewState @Inject constructor() : BaseState(), IWebView.State {
    override var pageUrl: String? = ""
}