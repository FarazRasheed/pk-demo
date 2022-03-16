package com.yap.yappk.pk.ui.kyc.cardname.cardnameconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ICardNameConfirmation {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val isCardNameSaved: LiveData<Boolean>
        val openCardPaymentAddCard: LiveData<SingleEvent<Int>>
        val openAddressConfirmation: LiveData<SingleEvent<Int>>
        val openCardDashBoard: LiveData<SingleEvent<Int>>
        fun openAddCardScreen()
        fun openAddressScreen()
        fun openCardDashboardScreen()
        fun saveCardName(cardName: String)
    }

    interface State : IBase.State {
        val cardName: MutableLiveData<String>
    }
}