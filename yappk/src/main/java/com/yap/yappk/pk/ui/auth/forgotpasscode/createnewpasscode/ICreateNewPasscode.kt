package com.yap.yappk.pk.ui.auth.forgotpasscode.createnewpasscode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.requestsdtos.CreateNewPasscodeRequest

interface ICreateNewPasscode {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var mobileNumber: String
        var optToken: String?
        val isPasscodeCreated: LiveData<Boolean>
        val openCreateNewPasscodeSuccess: LiveData<SingleEvent<Int>>
        val sharedPreferenceManager: SharedPreferenceManager
        fun createNewPasscode(createNewPasscodeRequest: CreateNewPasscodeRequest)
        fun openCreateNewPasscodeSuccessScreen()
        fun handlePressOnCreatePasscode()
        fun savePassCode()
        fun isBiometricLoginEnabled(isBiometricAvailable: Boolean): Boolean
    }

    interface State : IBase.State {
        var passcode: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var passcodeError: MutableLiveData<String>
    }

}
