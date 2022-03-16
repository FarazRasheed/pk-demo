package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.currentcardpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ICurrentCardPin {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val openChangeCardPin: LiveData<SingleEvent<Int>>
        val openForgotPin: LiveData<SingleEvent<Int>>
        val verifyCardPinSuccess: LiveData<Boolean>
        fun navigateToEnterNewPinScreen()
        fun navigateToForgotPinScreen()
        fun verifyCardPin(cardPin: String, cardSerialNumber: String)
    }

    interface State : IBase.State {
        var pin: MutableLiveData<String>
        var pinError: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
    }
}
