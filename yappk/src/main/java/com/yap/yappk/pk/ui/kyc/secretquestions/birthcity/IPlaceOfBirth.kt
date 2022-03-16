package com.yap.yappk.pk.ui.kyc.secretquestions.birthcity

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.requestsdtos.VerifySecretQuestionsRequest
import com.yap.yappk.pk.ui.kyc.secretquestions.adapter.SecretQuestionsAdapter

interface IPlaceOfBirth {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val secretQuestionsAdapter: SecretQuestionsAdapter
        val cityOfBirthNamesList: LiveData<MutableList<String>>
        val isVerified: LiveData<Boolean>
        var placeOfBirthCityAnswer: String?
        var mothersMaidenNameAnswer: String?
        fun verifyQuestionAnswers(request: VerifySecretQuestionsRequest)
    }

    interface State : IBase.State
}