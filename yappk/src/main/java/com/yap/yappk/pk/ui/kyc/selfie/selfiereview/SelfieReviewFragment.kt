package com.yap.yappk.pk.ui.kyc.selfie.selfiereview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentSelfieReviewBinding
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.ui.kyc.selfie.takeselfie.TakeSelfieFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelfieReviewFragment :
    BaseNavFragment<FragmentSelfieReviewBinding, ISelfieReview.State, ISelfieReview.ViewModel>(R.layout.fragment_selfie_review),
    ISelfieReview.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISelfieReview.ViewModel by viewModels<SelfieReviewVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
        viewModelObservers()
    }

    override fun viewModelObservers() {
        observeEvent(viewModel.openCardNameConfirmation, ::openCardSchemeSelectionScreen)
        observe(viewModel.isSelfieUploaded, ::handleSelfieUploaded)
    }

    private fun handleSelfieUploaded(isCompleteVerification: Boolean) {
        if (isCompleteVerification) viewModel.openCardSchemeSelectionScreen()
    }

    private fun openCardSchemeSelectionScreen(navigationEvent: SingleEvent<Int>) {
        navigationEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(destinationId, R.id.selfieGuideFragment)
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnReTake -> onStartIconClicked()
            R.id.btnYes -> viewModel.viewState.filePath.value?.let {
                viewModel.uploadSelfie(it)
            }
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.viewState.filePath.value = it.getString(TakeSelfieFragment::class.java.name)
        }
    }
}