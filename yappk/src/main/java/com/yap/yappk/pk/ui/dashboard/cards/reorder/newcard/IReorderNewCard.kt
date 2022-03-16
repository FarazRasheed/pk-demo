package com.yap.yappk.pk.ui.dashboard.cards.reorder.newcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.cards.responsedtos.Address

interface IReorderNewCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun requestGetAddressForPhysicalCard()
        fun getPhysicalCardFee()
        fun getCardBalance(cardSerialNumber: String?)
        fun reorderDebitCard(address: Address?)
        val reorderCardSuccess: LiveData<Boolean>
        var address: MutableLiveData<Address>?
    }

    interface State : IBase.State {
        var availableBalance: MutableLiveData<String>
        var reorderNewCardFee: MutableLiveData<String>
        var cardAddressTitle: MutableLiveData<String>?
        var cardAddressSubTitle: MutableLiveData<String>?
    }
}