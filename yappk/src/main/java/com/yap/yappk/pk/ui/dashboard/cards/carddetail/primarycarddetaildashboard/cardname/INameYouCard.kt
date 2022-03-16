package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardname

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface INameYouCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun setCardNameRequest(cardName: String, cardSerialNumber: String)
        fun navigateToCardDashboard()
        val setCardNameSuccess: LiveData<Boolean>
        val navigateToCardDashboard: LiveData<SingleEvent<Int>>
    }

    interface State : IBase.State {
        var valid: MutableLiveData<Boolean>
    }
}