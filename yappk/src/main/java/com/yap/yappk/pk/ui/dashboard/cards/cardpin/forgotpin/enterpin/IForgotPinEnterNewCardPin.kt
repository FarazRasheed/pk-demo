package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.enterpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface IForgotPinEnterNewCardPin {

    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var confirmPin: String
        val openConfirmPin: LiveData<SingleEvent<Int>>
        val openForgotCardPinSuccess: LiveData<SingleEvent<Int>>
        val openVerifyOtp: LiveData<SingleEvent<Int>>
        val isOtpCreated: LiveData<Boolean>
        val isForgotCardPinOtpCreated: LiveData<Boolean>
        val isCardPinCreated: LiveData<Boolean>
        fun openCardConfirmPinScreen()
        fun handlePressOnNext()
        fun handlePressOnCreate()
        fun openVerifyOtpScreen()
        fun openForgotCardPinSuccessScreen()
        fun createOtpForgotCardPin(action: String)
        fun createForgotCardPin(cardSerialNumber: String, newPin: String, otpToken: String)
    }

    interface State : IBase.State {
        var pin: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var isConfirmedPin: MutableLiveData<Boolean>
        var pinError: MutableLiveData<String>
    }
}