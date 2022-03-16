package com.yap.yappk.pk.ui.kyc.uploaddocument.scan

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class KycScanDocumentState @Inject constructor() : BaseState(), IKycScanDocument.State {
    override val documentIssueDate: MutableLiveData<String> = MutableLiveData("01/01/2020")
    override val cnicNumber: MutableLiveData<String> = MutableLiveData()
}
