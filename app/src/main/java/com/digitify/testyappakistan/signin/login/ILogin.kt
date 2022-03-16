package com.digitify.testyappakistan.signin.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ILogin {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val verifyUserEvent: LiveData<Boolean>
        val isAccountBlocked: LiveData<Boolean>
        val openAuthentication: LiveData<SingleEvent<String>>
        fun verifyUser(countryCode: String?, mobileNo: String?)
        fun openAuthenticationScreen()
    }

    interface State : IBase.State {
        var isValid: MutableLiveData<Boolean>
        val mobile: MutableLiveData<String>
        var countryCode: ObservableField<String>
        var isRemember: ObservableBoolean
        var isError: ObservableBoolean
        var selectedRegion: MutableLiveData<String?>
    }
}