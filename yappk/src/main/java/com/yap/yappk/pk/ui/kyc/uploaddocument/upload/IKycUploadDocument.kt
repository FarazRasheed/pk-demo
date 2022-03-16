package com.yap.yappk.pk.ui.kyc.uploaddocument.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.digitify.identityscanner.docscanner.enums.DocumentType
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult
import com.yap.yappk.networking.microservices.customers.responsedtos.documents.IDCardDetect
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.enums.DocScanStatus

interface IKycUploadDocument {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val openDocumentScanner: LiveData<SingleEvent<DocumentType>>
        val openDocumentScanResult: LiveData<SingleEvent<Int>>
        val documentsScanned: LiveData<Boolean>
        var detectIDCard: IDCardDetect?
        fun openDocumentScanResultScreen()
        fun canOpenDocumentScanner(): Boolean
        fun openScanner()
        fun getDocumentBy(type: DocumentType)
        fun uploadDocumentForScan(result: IdentityScannerResult)
    }

    interface State : IBase.State {
        var documentScanStatus: MutableLiveData<DocScanStatus>
    }
}