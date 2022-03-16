package com.yap.yappk.pk.ui.kyc.uploaddocument.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.enums.DocScanStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class KycUploadDocumentVM @Inject constructor(
    override val viewState: KycUploadSDocumentState,
    override val sessionManager: SessionManager,
    private val customersApi: CustomersApi
) : BaseViewModel<IKycUploadDocument.State>(), IKycUploadDocument.ViewModel {

    private val _openDocumentScanner: MutableLiveData<SingleEvent<DocumentType>> = MutableLiveData()
    override val openDocumentScanner: LiveData<SingleEvent<DocumentType>> = _openDocumentScanner

    override var detectIDCard: IDCardDetect? = null

    private val _openDocumentScanResult: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openDocumentScanResult: LiveData<SingleEvent<Int>> = _openDocumentScanResult

    private val _documentsScanned: MutableLiveData<Boolean> = MutableLiveData()
    override val documentsScanned: LiveData<Boolean> = _documentsScanned

    override fun onCreate() {
        super.onCreate()
        getDocumentBy(DocumentType.CNIC)
    }

    override fun canOpenDocumentScanner(): Boolean {
        return viewState.documentScanStatus.value != DocScanStatus.DOCS_UPLOADED
                && viewState.documentScanStatus.value != DocScanStatus.SCAN_COMPLETED
    }

    override fun openScanner() {
        _openDocumentScanner.value = SingleEvent(DocumentType.CNIC)
    }

    override fun openDocumentScanResultScreen() {
        _openDocumentScanResult.value =
            SingleEvent(R.id.action_kycUploadDocumentFragment_to_kycScanDocumentFragment)
    }

    override fun uploadDocumentForScan(result: IdentityScannerResult) {
        if (!result.document.files.isNullOrEmpty() && result.document.files.size < 3) {
            val file = File(result.document.files[0].croppedFile)
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

    override fun getDocumentBy(type: DocumentType) {
        launch {
            showLoading(true)
            val response = customersApi.getDocumentsByType("CNIC")
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        if (response.data.data != null)
                            viewState.documentScanStatus.value = DocScanStatus.DOCS_UPLOADED
                        else
                            viewState.documentScanStatus.value = DocScanStatus.SCAN_PENDING

                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        viewState.documentScanStatus.value = DocScanStatus.NONE
                    }
                }
            }
        }
    }
}