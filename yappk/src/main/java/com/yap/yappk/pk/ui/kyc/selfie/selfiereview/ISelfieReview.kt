package com.yap.yappk.pk.ui.kyc.selfie.selfiereview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ISelfieReview {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val isSelfieUploaded: LiveData<Boolean>
        val openCardNameConfirmation: LiveData<SingleEvent<Int>>
        fun openCardSchemeSelectionScreen()
        fun uploadSelfie(selfiePath: String)
    }

    interface State : IBase.State {
        val filePath: MutableLiveData<String>
    }
}