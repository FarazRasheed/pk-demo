package com.yap.yappk.pk.ui.kyc.selfie.takeselfie

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class TakeSelfieState @Inject constructor() : BaseState(), ITakeSelfie.State {
    override val hasFace: MutableLiveData<Boolean> = MutableLiveData()
}