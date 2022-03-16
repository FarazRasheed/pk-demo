package com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.utils.DateUtils
import com.digitify.core.utils.DateUtils.DEFAULT_DATE_FORMAT
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentDocumentReviewBinding
import com.yap.yappk.networking.microservices.customers.requestsdtos.UploadDocumentsRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.DocumentDetails
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.KYC_DOCUMENT_DATA
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DocumentReviewFragment :
    BaseNavFragment<FragmentDocumentReviewBinding, IDocumentReview.State, IDocumentReview.ViewModel>(
        R.layout.fragment_document_review
    ), IDocumentReview.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IDocumentReview.ViewModel by viewModels<DocumentReviewVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                uploadDocumentData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        viewModelObservers()
    }

    private fun init() {
        val documentData = arguments?.getParcelable<DocumentDetails>(KYC_DOCUMENT_DATA)
        documentData?.run {
            viewModel.viewState.firstName.value = name
            viewModel.viewState.gender.value = if (gender == "M") "Male" else "Female"
            viewModel.viewState.dateOfBirth.value = covertDate(dob)
            viewModel.viewState.documentIssueDate.value = covertDate(issueDate)
            viewModel.viewState.expiryDate.value = covertDate(expiryDate)
            viewModel.viewState.cnicNumber.value =
                viewModel.getFormattedCitizenNumber(citizenNumber)
            viewModel.viewState.residentialAddress.value = residentialAddress
        }
    }

    private fun uploadDocumentData() {
        val request = UploadDocumentsRequest(
            documentType = "CNIC",
            fullName = viewModel.viewState.firstName.value,
            identityNo = viewModel.viewState.cnicNumber.value?.replace("-", ""),
            nationality = "PAK",
            gender = if (viewModel.viewState.gender.value == "Male") "M" else "F",
            dateExpiry = getStringIntoDate(viewModel.viewState.expiryDate.value),
            dob = getStringIntoDate(viewModel.viewState.dateOfBirth.value),
            dateIssue = getStringIntoDate(viewModel.viewState.documentIssueDate.value),
            filePaths = parentViewModel.paths
        )

        viewModel.uploadDocument(request)
    }

    private fun navigateToSecretQuestions(isUploaded: Boolean) {
        if (isUploaded) {
            navigateWithPopup(
                R.id.action_documentReviewFragment_to_mothersMaidenNameFragment,
                R.id.kycScanDocumentFragment
            )
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.documentUploaded, ::navigateToSecretQuestions)
    }

    private fun getStringIntoDate(strDate: String?): Date? {
        return DateUtils.stringToDate(
            strDate ?: "",
            DEFAULT_DATE_FORMAT
        )
    }

    private fun covertDate(strDate: String?): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(parser.parse(strDate))
    }

}