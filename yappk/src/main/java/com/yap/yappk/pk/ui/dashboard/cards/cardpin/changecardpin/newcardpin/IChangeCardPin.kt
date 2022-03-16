package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.newcardpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.pk.ui.dashboard.cards.models.ChangeCardPinExtras

interface IChangeCardPin {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val openConfirmCardPin: LiveData<SingleEvent<Int>>
        val changePinSuccess: LiveData<Boolean>
        fun checkPasscodeValidation()
        fun navigateToConfirmCardPinScreen()
        fun navigateToChangeCardPinSuccessScreen()
        fun changeCardPin(
            oldPin: String,
            newPin: String,
            confirmPin: String,
            cardSerialNumber: String
        )
    }

    interface State : IBase.State {
        var changeCardPinModel: MutableLiveData<ChangeCardPinExtras>
        var screenType: MutableLiveData<String>
        var pin: MutableLiveData<String>
        var pinError: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
    }
}