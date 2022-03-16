package com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.UploadDocumentsRequest
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DocumentReviewVM @Inject constructor(
    override val viewState: DocumentReviewState,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) : BaseViewModel<IDocumentReview.State>(), IDocumentReview.ViewModel {
    private val _documentUploaded: MutableLiveData<Boolean> = MutableLiveData()
    override val documentUploaded: LiveData<Boolean> = _documentUploaded

    override fun uploadDocument(uploadDocumentsRequest: UploadDocumentsRequest) {
        launch {
            showLoading(true)
            val response = customersApi.uploadDocument(uploadDocumentsRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _documentUploaded.value = false
                    }
                }
            }
        }
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _documentUploaded.value = true
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
                    _documentUploaded.value = false
                }
            }
        }
    }

    override fun getFormattedCitizenNumber(citizenNo: String?): String {
        try {
            return citizenNo?.let {
                val builder = StringBuilder()
                if (hasValidPart(it, 0, 4)) {
                    builder.append(it.subSequence(0..4))
                    builder.append("-")
                }
                if (hasValidPart(it, 5, 11)) {
                    builder.append(it.subSequence(5..11))
                    builder.append("-")
                }
                if (hasValidPart(it, 12, 12)) {
                    builder.append(it.subSequence(12..12))
                }
                return@let builder.toString()
            } ?: ""
        } catch (exception: IndexOutOfBoundsException) {
            showToast("Invalid CNIC number")
            return ""
        }
    }

    private fun hasValidPart(value: String?, start: Int, end: Int): Boolean {
        return value?.let {
            return (end in start..it.length)
        } ?: false
    }
}