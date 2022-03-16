package com.yap.yappk.pk.ui.kyc.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.visible
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.digitify.identityscanner.utils.deleteTempFolder
import com.yap.uikit.widget.progressbar.AnimatedProgressBar
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.KycDashboardActivityBinding
import com.yap.yappk.pk.utils.KYC_FROM_ONBOARDING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycDashboardActivity :
    BaseNavActivity<KycDashboardActivityBinding, IKycDashboard.State, IKycDashboard.ViewModel>(),
    IKycDashboard.View, AnimatedProgressBar.OnProgressWidgetButtonsClickListener {
    override fun getLayoutId(): Int = R.layout.kyc_dashboard_activity
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IKycDashboard.ViewModel by viewModels<KycDashboardVM>()
    override val navigationGraphId: Int = R.navigation.pk_kyc_dashboard_nav_graph
    override val navigationGraphStartDestination: Int
        get() = intent?.getIntExtra(NAVIGATION_GRAPH_START_DESTINATION_ID, 0) ?: 0

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        when (destination?.id) {
            R.id.takeSelfieFragment, R.id.selfieReviewFragment,
            R.id.updateCardNameFragment, R.id.addressSelectionFragment,
            R.id.cityNameFragment, R.id.cardSchemeBenefitsFragment,
            R.id.cardNameConfirmationFragment, R.id.cardPaymentAddCardFragment,
            R.id.cardPaymentDashboardFragment, R.id.topUpOtpVerificationFragment2, R.id.onBoardingCardCvvFragment -> mViewBinding.apbProgress.gone()
            R.id.cardNameConfirmationFragment, R.id.cardPaymentAddCardFragment,
            R.id.cardOrderPaymentConfirmationFragment -> mViewBinding.apbProgress.gone()
            R.id.kycUploadDocumentFragment, R.id.selfieGuideFragment,
            R.id.invalidCustomerFragment, R.id.validCustomerFragment -> {
                mViewBinding.apbProgress.invisible()
                viewModel.setProgressVisibility(false)
            }
            else -> {
                mViewBinding.apbProgress.visible()
                viewModel.setProgressVisibility(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding.apbProgress.setOnWidgetClickListener(this)

        if (intent != null)
            viewModel.isFromOnboarding = intent.getBooleanExtra(KYC_FROM_ONBOARDING, false)
        viewModelObservers()
    }

    private fun viewModelObservers() {
        observe(viewModel.toolbarVisibilityGone, ::toggleToolbarGone)
    }

    private fun toggleToolbarGone(isGone: Boolean) {
        if (isGone) mViewBinding.apbProgress.gone() else mViewBinding.apbProgress.visible()
    }

    override fun onBackButtonClicked() {
        onBackPressed()
    }

    override fun onDoneButtonClicked() {
        // to be handle late
    }

    override fun onDestroy() {
        this.deleteTempFolder()
        super.onDestroy()
    }
}