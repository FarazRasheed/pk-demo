package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ICardPinEnter {
    interface View : IBase.View<ViewModel>{
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val openConfirmPin: LiveData<SingleEvent<Int>>
        fun handlePressOnNext()
        fun openCardConfirmPinScreen()
    }

    interface State : IBase.State {
        var pin: MutableLiveData<String>
        var valid: MutableLiveData<Boolean>
        var pinError: MutableLiveData<String>
    }
}