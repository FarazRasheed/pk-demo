package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.cards.requestdtos.CardPinRequest

interface ICardConfirmPin {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var confirmPin: String
        val openCardPinSuccess: LiveData<SingleEvent<Int>>
        val isPinCodeSet: LiveData<Boolean>
        fun handlePressOnCreate()
        fun openCardPinSuccessScreen()
        fun getCardPinRequest(): CardPinRequest
        fun setCardPin(cardPin: String, cardSerialNumber: String)
    }

    interface State : IBase.State {
        var newPin: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var pinError: MutableLiveData<String>
    }
}