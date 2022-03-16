package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.uikit.widget.webview.AdvancedWebView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAddExternalCardBinding
import com.yap.yappk.databinding.LayoutCardAddedSuccessDialogViewBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard.addcardsuccessdialog.AddCardSuccessDialogVH
import com.yap.yappk.pk.ui.dashboard.yapit.enum.*
import com.yap.yappk.pk.utils.BaseViewHolder
import com.yap.yappk.pk.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddExternalCardFragment :
    BaseNavFragment<FragmentAddExternalCardBinding, IAddExternalCard.State, IAddExternalCard.ViewModel>(
        R.layout.fragment_add_external_card
    ),
    IAddExternalCard.View, ToolBarView.OnToolBarViewClickListener, AdvancedWebView.Listener,
    MultiStateLayout.OnReloadListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IAddExternalCard.ViewModel by viewModels<AddExternalCardVM>()
    private var mCardSuccessDialogVH: BaseViewHolder? = null
    var mCardSuccessAlertDialog: AlertDialog? = null

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        initAdvanceWebView()
    }

    override fun onStartIconClicked() {
        navigateBack()
        context?.hideKeyboard()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> openCardDetailDialog()
        }
    }

    private fun openCardDetailDialog() {
        mCardSuccessDialogVH =
            AddCardSuccessDialogVH(cardDialogViewClickListener)
        mCardSuccessDialogVH?.let { dialogVH ->
            mCardSuccessAlertDialog = requireActivity().showDialog(dialogVH, true) {
                LayoutCardAddedSuccessDialogViewBinding.inflate(layoutInflater)
            }
        }
    }

    private val cardDialogViewClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.btnTopUp -> {
                    mCardSuccessAlertDialog?.dismiss()
                    backToDashBoard(true)
                }
                R.id.btnDoItLater -> {
                    mCardSuccessAlertDialog?.dismiss()
                    backToDashBoard(false)
                }
            }
        }
    }

    private fun backToDashBoard(isTopUp: Boolean) {
        viewModel.card.value?.isTopUp = isTopUp
        navigateBackWithResult(
            Activity.RESULT_OK,
            bundleOf(ExternalCard::class.java.name to viewModel.card.value)
        )
    }

    private fun initAdvanceWebView() {
        mViewBinding.webView.setListener(this, this)
        mViewBinding.webView.setGeolocationEnabled(false)
        mViewBinding.webView.setMixedContentAllowed(true)
        mViewBinding.webView.setCookiesEnabled(true)
        mViewBinding.webView.setThirdPartyCookiesEnabled(true)
        mViewBinding.webView.addHttpHeader(CARD_HTTP_HEADER, "")
        loadCardPage()
    }

    private fun loadCardPage() {
        mViewBinding.webView.loadUrl(
            viewModel.getAddCardPageUrl(
                viewModel.pkBuildConfigurations.configManager?.flavor ?: ""
            )
        )
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        url?.let {
            mViewBinding.multiStateView.viewState = StateEnum.LOADING
        }
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
            when {
                it.url.toString().startsWith(CARD_RETURN_SCHEME) -> {
                    getAddedCard(it.url)
                    return true
                }
                else -> return false
            }
        }
        return false
    }

    private fun getAddedCard(url: Uri) {
        when (val errors = url.getQueryParameter(CARD_ERRORS)) {
            null -> viewModel.addExternalCard(
                url.getQueryParameter(SESSIONID).toString(),
                url.getQueryParameter(CARD_ALIAS).toString(),
                url.getQueryParameter(CARD_COLOR).toString(),
                url.getQueryParameter(CARD_NUMBER).toString()
            )
            else -> {
                showToast(errors.toString())
                loadCardPage()
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        mViewBinding.webView.onActivityResult(requestCode, resultCode, intent)
    }

    private fun handleCard(card: ExternalCard?) {
        card?.let {
            openCardDetailDialog()
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.card, ::handleCard)
    }

}