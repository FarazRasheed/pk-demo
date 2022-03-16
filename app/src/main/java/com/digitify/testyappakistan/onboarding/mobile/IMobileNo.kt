package com.digitify.testyappakistan.onboarding.mobile

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface IMobileNo {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var otpCreateEvent: LiveData<Boolean>
        val showError: LiveData<SingleEvent<String?>>
        fun createOtpUserVerifier(countryCode: String, mobileNumber: String)
    }

    interface State : IBase.State {
        var isValid: MutableLiveData<Boolean>
        val mobile: MutableLiveData<String>
        var countryCode: ObservableField<String>
        var selectedRegion: MutableLiveData<String?>
    }
}