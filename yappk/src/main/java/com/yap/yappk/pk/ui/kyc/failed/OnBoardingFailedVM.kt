package com.yap.yappk.pk.ui.kyc.failed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingFailedVM @Inject constructor(
    override val viewState: OnBoardingFailedState,
    private val onBoardingFailedComposer: IOnBoardingFailedDataComposer
) :
    BaseViewModel<IOnBoardingFailed.State>(), IOnBoardingFailed.ViewModel {

    private var _onBoardingFailedDataModel: MutableLiveData<OnBoardingFailedModel> = MutableLiveData()
    override var onBoardingFailedDataModel: LiveData<OnBoardingFailedModel> = _onBoardingFailedDataModel

    override fun setOnBoardingFailedStateBy(onBoardingFailedStatus: String) {
        _onBoardingFailedDataModel.value = onBoardingFailedComposer.composeData(onBoardingFailedStatus)
    }
}
