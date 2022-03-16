package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.verifypasscode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface IForgotPinVerifyPasscode {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val openEnterNewCardPinScreen: LiveData<SingleEvent<Int>>
        val isPasscodeVerified: LiveData<Boolean>
        fun openEnterNewCardPinScreen()
        fun handlePressOnVerify()
        fun verifyPasscode(passcode: String)
    }

    interface State : IBase.State {
        var passcode: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var passcodeError: MutableLiveData<String>
    }
}