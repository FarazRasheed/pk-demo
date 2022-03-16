package com.yap.yappk.pk.ui.kyc.uploaddocument.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.DocumentDetailRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.DocumentDetails
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KycScanDocumentVM @Inject constructor(
    override val viewState: KycScanDocumentState,
    override val sessionManager: SessionManager,
    private val customersApi: CustomersApi
) : BaseViewModel<IKycScanDocument.State>(), IKycScanDocument.ViewModel {

    private val _documentDetails: MutableLiveData<DocumentDetails> = MutableLiveData()
    override val documentDetails: LiveData<DocumentDetails> = _documentDetails

    private val _openDocumentDetails: MutableLiveData<SingleEvent<DocumentDetails>> =
        MutableLiveData()
    override val openDocumentDetails: LiveData<SingleEvent<DocumentDetails>> = _openDocumentDetails

    private val _documentsScanned: MutableLiveData<Boolean> = MutableLiveData()
    override val documentsScanned: LiveData<Boolean> = _documentsScanned

    private val _openDocumentScanner: MutableLiveData<SingleEvent<DocumentType>> = MutableLiveData()
    override val openDocumentScanner: LiveData<SingleEvent<DocumentType>> = _openDocumentScanner

    override var detectIDCard: IDCardDetect? = null

    override fun getDocumentDetails(documentDetailRequest: DocumentDetailRequest) {
        launch {
            showLoading(true)
            val response = customersApi.getDocumentDetail(documentDetailRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _documentDetails.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(message = response.error.message)
                        _documentDetails.value = null
                    }
                }
            }
        }
    }

    override fun uploadDocumentForScan(result: IdentityScannerResult) {
        if (!result.document.files.isNullOrEmpty() && result.document.files.size < 3) {
            val file = File(result.document.files[1].croppedFile)
            val fileReqBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("files", file.name, fileReqBody)
            launch {
                showLoading(true)
                val response = customersApi.uploadIDCard(part)
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ApiResponse.Success -> {
                            hideLoading(true)
                            detectIDCard = response.data.data
                            _documentsScanned.value = true
                        }
                        is ApiResponse.Error -> {
                            hideLoading()
                            _documentsScanned.value = false
                            showAlertMessage(response.error.message)
                        }
                    }
                }
            }
        } else {
            _documentsScanned.value = false
            showAlertMessage("There is some issue with captured images")
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

    override fun openDocumentDetails(documentDetails: DocumentDetails) {
        _openDocumentDetails.value = SingleEvent(documentDetails)
    }

    override fun openScanner() {
        _openDocumentScanner.value = SingleEvent(DocumentType.CNIC)
    }

}