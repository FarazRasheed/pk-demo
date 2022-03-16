package com.yap.yappk.pk.ui.kyc.uploaddocument.upload

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finish
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.activities.IdentityScannerActivity
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentKycUploadDocumentBinding
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import com.yap.yappk.pk.ui.kyc.KYCRouteManager
import com.yap.yappk.pk.ui.kyc.KycRoute
import com.yap.yappk.pk.ui.kyc.KycRoute.*
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KycUploadDocumentFragment :
    BaseNavFragment<FragmentKycUploadDocumentBinding, IKycUploadDocument.State, IKycUploadDocument.ViewModel>(
        R.layout.fragment_kyc_upload_document
    ), IKycUploadDocument.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IKycUploadDocument.ViewModel by viewModels<KycUploadDocumentVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    lateinit var kycRouteManager: KYCRouteManager

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                val kycRoute =
                    kycRouteManager.getKycScreenRoute(viewModel.sessionManager.userAccount.value)

                navigateTo(kycRoute)
            }

            R.id.btnSkip -> {
                if (parentViewModel.isFromOnboarding)
                    launchActivity<PkDashboardActivity>(clearPrevious = true)
                else
                    finish()

            }

            R.id.cvCard -> {
                if (viewModel.canOpenDocumentScanner())
                    viewModel.openScanner()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        kycRouteManager = KYCRouteManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
        parentViewModel.setProgressVisibility(false)
    }

    private fun navigateTo(route: KycRoute) {
        when (route) {
            SECRET_QUESTION_SCREEN -> navigate(R.id.mothersMaidenNameFragment)
            SELFIE_SCREEN -> navigate(R.id.selfieGuideFragment)
            CARD_SCHEME_SCREEN -> navigate(R.id.selectCardSchemeFragment)
            NONE -> {
                showToast("Under development")
            }
        }
    }

    private fun openDocumentScanner(navigateEvent: SingleEvent<DocumentType>) {
        navigateEvent.getContentIfNotHandled()?.let { documentType ->
            val intent = IdentityScannerActivity.getLaunchIntent(
                requireContext(),
                documentType,
                IdentityScannerActivity.SCAN_FROM_CAMERA
            )
            activityLauncher.launch(intent) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let {
                        val documentsImages =
                            it.getParcelableExtra<IdentityScannerResult>(IdentityScannerActivity.SCAN_RESULT)
                        documentsImages?.document?.let { documents ->
                            parentViewModel.paths.add(documents.files[0].croppedFile)
                            parentViewModel.paths.add(documents.files[1].croppedFile)
                            viewModel.uploadDocumentForScan(documentsImages)
                        }
                    }
                }
            }
        }
    }

    private fun openDocumentScanResultScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(
                destinationId,
                bundleOf("CNIC_ISSUE_DATE" to viewModel.detectIDCard?.issueDate)
            )
        }
    }

    private fun handleDocumentScannedResult(isScanned: Boolean) {
        if (isScanned) {
            viewModel.detectIDCard?.citizenNumber?.let {
                parentViewModel.citizenNumber = it
            }
            viewModel.openDocumentScanResultScreen()
        }
    }

    override fun viewModelObservers() {
        observeEvent(viewModel.openDocumentScanner, ::openDocumentScanner)
        observeEvent(viewModel.openDocumentScanResult, ::openDocumentScanResultScreen)
        observe(viewModel.documentsScanned, ::handleDocumentScannedResult)
    }

}