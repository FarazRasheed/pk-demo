package com.yap.yappk.pk.ui.kyc.failed

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase

interface IOnBoardingFailed {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments(): String?
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun setOnBoardingFailedStateBy(onBoardingFailedStatus: String)
        var onBoardingFailedDataModel: LiveData<OnBoardingFailedModel>
    }

    interface State : IBase.State
}