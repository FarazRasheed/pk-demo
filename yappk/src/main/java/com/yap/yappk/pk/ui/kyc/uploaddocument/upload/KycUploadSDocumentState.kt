package com.yap.yappk.pk.ui.kyc.uploaddocument.upload

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import com.yap.yappk.pk.ui.kyc.enums.DocScanStatus
import javax.inject.Inject

class KycUploadSDocumentState @Inject constructor() : BaseState(), IKycUploadDocument.State {
    override var documentScanStatus: MutableLiveData<DocScanStatus> =
        MutableLiveData()
}