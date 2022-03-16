package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.otpverification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.uikit.widget.webview.AdvancedWebView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentTopUpOtpVerificationBinding
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer.TopUpViaExternalCardFragment
import com.yap.yappk.pk.ui.dashboard.yapit.enum.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopUpOtpVerificationFragment :
    BaseNavFragment<PkFragmentTopUpOtpVerificationBinding, ITopUpOtpVerification.State, ITopUpOtpVerification.ViewModel>(
        R.layout.pk_fragment_top_up_otp_verification
    ), ITopUpOtpVerification.View, ToolBarView.OnToolBarViewClickListener, AdvancedWebView.Listener,
    MultiStateLayout.OnReloadListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ITopUpOtpVerification.ViewModel by viewModels<TopUpOtpVerificationVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initAdvanceWebView()
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        viewModel.setUrl(arguments?.getString(TopUpOtpVerificationFragment::class.java.name))
    }

    override fun onClick(id: Int) {
        when (id) {
            //later
        }
    }

    override fun onStartIconClicked() {
        hideKeyboard()
        navigateBack()
    }

    private fun initAdvanceWebView() {
        mViewBinding.webView.setListener(this, this)
        mViewBinding.webView.setGeolocationEnabled(false)
        mViewBinding.webView.setMixedContentAllowed(true)
        mViewBinding.webView.setCookiesEnabled(true)
        mViewBinding.webView.setThirdPartyCookiesEnabled(true)
        mViewBinding.webView.addHttpHeader(CARD_HTTP_HEADER, "")
        mViewBinding.webView.setGeolocationEnabled(true)
        mViewBinding.webView.settings.setSupportZoom(true)

    }

    private fun loadCardPage(url: String) {
        val base64 =
            android.util.Base64.encodeToString(
                url.toByteArray(),
                android.util.Base64.NO_PADDING
            )
        mViewBinding.webView.loadData(base64, OTP_HTTP_MIM_TYPE, OTP_HTTP_ENCODING)
    }

    private fun handleUrl(url: String) {
        loadCardPage(url)
    }

    override fun onReload(view: View) = Unit

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mViewBinding.webView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        mViewBinding.webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mViewBinding.webView.onDestroy()
        super.onDestroy()
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        url?.let {
            when {
                it.contains(OTP_VERIFICATION_SCHEME) || it.contains(OTP_TRANSACTION) -> {
                    mViewBinding.webView.invisible()
                    navigateBackWithSuccess()
                }
                else -> mViewBinding.multiStateView.viewState = StateEnum.LOADING
            }
        }
    }

    private fun navigateBackWithSuccess() {
        navigateBackWithResult(resultCode = Activity.RESULT_OK)
    }

    override fun onPageFinished(url: String?) {
        mViewBinding.multiStateView.viewState = StateEnum.CONTENT
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        mViewBinding.multiStateView.viewState = StateEnum.EMPTY
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) = Unit

    override fun onExternalPageRequest(url: String?) = Unit

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.let {
            return when {
                it.url.toString().startsWith(CARD_RETURN_SCHEME) -> {
                    true
                }
                else -> false
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        mViewBinding.webView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun viewModelObservers() {
        observe(viewModel.htmlUrl, ::handleUrl)
    }
}
