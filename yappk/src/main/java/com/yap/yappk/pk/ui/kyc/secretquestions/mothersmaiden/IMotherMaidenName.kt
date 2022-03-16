package com.yap.yappk.pk.ui.kyc.secretquestions.mothersmaiden

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.ui.kyc.secretquestions.adapter.SecretQuestionsAdapter

interface IMotherMaidenName {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val secretQuestionsAdapter: SecretQuestionsAdapter
        val motherNamesList: LiveData<MutableList<String>>
        var mothersName: String?
    }

    interface State : IBase.State
}