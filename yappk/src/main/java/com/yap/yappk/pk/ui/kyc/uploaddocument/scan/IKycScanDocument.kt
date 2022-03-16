package com.yap.yappk.pk.ui.kyc.uploaddocument.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.yap.yappk.networking.microservices.customers.requestsdtos.DocumentDetailRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.DocumentDetails
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import com.yap.yappk.pk.SessionManager

interface IKycScanDocument {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val documentDetails: LiveData<DocumentDetails>
        val openDocumentDetails: LiveData<SingleEvent<DocumentDetails>>
        val openDocumentScanner: LiveData<SingleEvent<DocumentType>>
        val documentsScanned: LiveData<Boolean>
        var detectIDCard: IDCardDetect?
        fun getFormattedCitizenNumber(citizenNo: String?): String
        fun openDocumentDetails(documentDetails: DocumentDetails)
        fun getDocumentDetails(documentDetailRequest: DocumentDetailRequest)
        fun uploadDocumentForScan(result: IdentityScannerResult)
        fun openScanner()
    }

    interface State : IBase.State {
        val documentIssueDate: MutableLiveData<String>
        val cnicNumber: MutableLiveData<String>
    }
}