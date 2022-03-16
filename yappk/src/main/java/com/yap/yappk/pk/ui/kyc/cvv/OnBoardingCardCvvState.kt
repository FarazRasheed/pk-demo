package com.yap.yappk.pk.ui.kyc.cvv

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class OnBoardingCardCvvState @Inject constructor() : BaseState(), IOnBoardingCardCvv.State {
    override var cvv: MutableLiveData<String> = MutableLiveData()
}