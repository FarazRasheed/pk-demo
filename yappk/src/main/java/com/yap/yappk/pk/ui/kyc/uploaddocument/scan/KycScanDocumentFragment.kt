package com.yap.yappk.pk.ui.kyc.uploaddocument.scan

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.DateUtils
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.activities.IdentityScannerActivity
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentKycScanDocumentBinding
import com.yap.yappk.networking.microservices.customers.requestsdtos.DocumentDetailRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.DocumentDetails
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.KYC_DOCUMENT_DATA
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class KycScanDocumentFragment :
    BaseNavFragment<FragmentKycScanDocumentBinding, IKycScanDocument.State, IKycScanDocument.ViewModel>(
        R.layout.fragment_kyc_scan_document
    ), IKycScanDocument.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IKycScanDocument.ViewModel by viewModels<KycScanDocumentVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()
    private var cnicIssueDate: String = ""

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                val request = DocumentDetailRequest(
                    cnic = parentViewModel.citizenNumber,
                    dateOfIssuance = covertDate(viewModel.viewState.documentIssueDate.value!!)
                )
                viewModel.getDocumentDetails(request)
            }
            R.id.btnRescan -> viewModel.openScanner()
            R.id.tvIssueDate -> showDatePicker()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        viewModel.viewState.cnicNumber.value =
            viewModel.getFormattedCitizenNumber(parentViewModel.citizenNumber)
        parentViewModel.setProgressVisibility(visible = true)
        parentViewModel.setProgress(25)
    }

    private fun getFragmentArguments() {
        cnicIssueDate = arguments?.getString("CNIC_ISSUE_DATE") ?: ""
        viewModel.viewState.documentIssueDate.value =
            DateUtils.convertLongToDate(covertServerDate(cnicIssueDate))
    }

    private fun handleDocumentDetails(documentDetails: DocumentDetails?) {
        documentDetails?.let {
            viewModel.openDocumentDetails(documentDetails)
        }
    }

    private fun navigateToDocumentDetailsScreen(navigateEvent: SingleEvent<DocumentDetails>) {
        navigateEvent.getContentIfNotHandled()?.let {
            navigate(
                R.id.action_kycScanDocumentFragment_to_documentReviewFragment,
                bundleOf(KYC_DOCUMENT_DATA to it)
            )
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

    private fun handleDocumentScannedResult(isScanned: Boolean) {
        if (isScanned) {
            viewModel.detectIDCard?.citizenNumber?.let {
                parentViewModel.citizenNumber = it
                cnicIssueDate = viewModel.detectIDCard?.issueDate ?: ""
                viewModel.viewState.documentIssueDate.value =
                    DateUtils.convertLongToDate(covertServerDate(cnicIssueDate))
            }
        }
    }

    override fun viewModelObservers() {
        observeEvent(viewModel.openDocumentScanner, ::openDocumentScanner)
        observeEvent(viewModel.openDocumentDetails, ::navigateToDocumentDetailsScreen)
        observe(viewModel.documentDetails, ::handleDocumentDetails)
        observe(viewModel.documentsScanned, ::handleDocumentScannedResult)
    }

    private fun showDatePicker() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())

        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = covertServerDate(cnicIssueDate)
        cal.add(Calendar.DATE, 1)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select CNIC issue date")
                .setSelection(cal.timeInMillis)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.viewState.documentIssueDate.value = DateUtils.convertLongToDate(it)
        }

        datePicker.show(requireActivity().supportFragmentManager, "Date")
    }

    private fun covertServerDate(strDate: String?): Long {
        return strDate?.let {
            DateUtils.stringToDate(strDate, "ddMMyy")?.time
        } ?: MaterialDatePicker.todayInUtcMilliseconds()
    }

    private fun covertDate(strDate: String): String {
        val parser = SimpleDateFormat("dd/MM/yyyy")
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(parser.parse(strDate))
    }

}