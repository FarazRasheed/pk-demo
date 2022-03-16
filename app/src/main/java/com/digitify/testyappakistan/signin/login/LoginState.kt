package com.digitify.testyappakistan.signin.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class LoginState @Inject constructor() : BaseState(), ILogin.State {
    override var isValid: MutableLiveData<Boolean> = MutableLiveData(false)
    override val mobile: MutableLiveData<String> = MutableLiveData("")
    override var countryCode: ObservableField<String> = ObservableField("+92")
    override var isRemember: ObservableBoolean = ObservableBoolean(false)
    override var isError: ObservableBoolean = ObservableBoolean(false)
    override var selectedRegion: MutableLiveData<String?> = MutableLiveData()

}