package com.yap.yappk.pk.ui.onboarding.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseFragment
import com.digitify.core.extensions.makeCall
import com.digitify.core.extensions.sendEmail
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.webview.AdvancedWebView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment :
    BaseFragment<PkFragmentWebviewBinding, IWebView.State, WebViewVM>(R.layout.pk_fragment_webview),
    AdvancedWebView.Listener, View.OnKeyListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: WebViewVM by viewModels()
    override fun onClick(id: Int) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.pageUrl = arguments?.getString("PAGE_URL", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdvanceWebView()
    }

    private fun initAdvanceWebView() {
        val webView = mViewBinding.webView
        webView.setListener(activity, this)
        webView.setGeolocationEnabled(false)
        webView.setMixedContentAllowed(false)
        webView.setCookiesEnabled(true)
        webView.setThirdPartyCookiesEnabled(true)
        webView.setOnKeyListener(this)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mViewBinding.multiStateLayout.viewState = StateEnum.CONTENT
            }
        }
        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)

            }

        }
        webView.addHttpHeader("X-Requested-With", "")
        webView.loadUrl(viewModel.viewState.pageUrl ?: "")
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        mViewBinding.multiStateLayout.viewState = StateEnum.LOADING
    }

    override fun onPageFinished(url: String?) {
        mViewBinding.multiStateLayout.viewState = StateEnum.CONTENT

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        mViewBinding.multiStateLayout.viewState = StateEnum.EMPTY

    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }


    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url.toString().startsWith("tel:")) {
            requireContext().makeCall(request?.url.toString().replaceFirst("tel:", ""))
            return true
        } else {
            if (request?.url.toString().startsWith("mailto")) {
                requireContext().sendEmail(
                    email = request?.url.toString().replaceFirst("mailto:", "")
                )
                return true
            }

        }
        return false
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mViewBinding.webView.onResume()
//        getActivityViewModel<OnBoardingVM>().viewState.progressBarVisibility.value = false
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        mViewBinding.webView.onPause()
//        getActivityViewModel<OnBoardingVM>().viewState.progressBarVisibility.value = true
        super.onPause()
    }

    override fun onDestroy() {
        mViewBinding.webView.onDestroy()
        super.onDestroy()
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event?.action == MotionEvent.ACTION_UP
            && mViewBinding.webView.canGoBack()
        ) {
            mViewBinding.webView.goBack()
            return true
        }
        return false
    }

}