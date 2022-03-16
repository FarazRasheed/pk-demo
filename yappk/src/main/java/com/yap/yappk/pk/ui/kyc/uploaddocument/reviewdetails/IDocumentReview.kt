package com.yap.yappk.pk.ui.kyc.uploaddocument.reviewdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.requestsdtos.UploadDocumentsRequest

interface IDocumentReview {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val documentUploaded: LiveData<Boolean>
        fun getFormattedCitizenNumber(citizenNo: String?): String
        fun uploadDocument(uploadDocumentsRequest: UploadDocumentsRequest)
    }

    interface State : IBase.State {
        val firstName: MutableLiveData<String>
        val lastName: MutableLiveData<String>
        val gender: MutableLiveData<String>
        val dateOfBirth: MutableLiveData<String>
        val documentIssueDate: MutableLiveData<String>
        val expiryDate: MutableLiveData<String>
        val residentialAddress: MutableLiveData<String>
        val cnicNumber: MutableLiveData<String>
    }
}