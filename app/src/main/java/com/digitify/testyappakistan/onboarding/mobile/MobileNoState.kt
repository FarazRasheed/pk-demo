package com.digitify.testyappakistan.onboarding.mobile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState

class MobileNoState : BaseState(), IMobileNo.State {
    override var isValid: MutableLiveData<Boolean> = MutableLiveData(false)
    override val mobile: MutableLiveData<String> = MutableLiveData("")
    override var countryCode: ObservableField<String> = ObservableField("+92")
    override var selectedRegion: MutableLiveData<String?> = MutableLiveData()
}