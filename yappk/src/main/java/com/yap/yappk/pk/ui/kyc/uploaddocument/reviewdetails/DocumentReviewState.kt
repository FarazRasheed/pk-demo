package com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class DocumentReviewState @Inject constructor() : BaseState(), IDocumentReview.State {
    override val firstName: MutableLiveData<String> = MutableLiveData()
    override val lastName: MutableLiveData<String> = MutableLiveData()
    override val gender: MutableLiveData<String> = MutableLiveData()
    override val dateOfBirth: MutableLiveData<String> = MutableLiveData()
    override val documentIssueDate: MutableLiveData<String> = MutableLiveData()
    override val expiryDate: MutableLiveData<String> = MutableLiveData()
    override val residentialAddress: MutableLiveData<String> = MutableLiveData()
    override val cnicNumber: MutableLiveData<String> = MutableLiveData()
}
