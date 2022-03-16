package com.yap.yappk.pk.ui.dashboard.cards.carditem

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState

interface ICardItem {

    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun notifyChangeFragment()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val cardState: LiveData<CardState>
        val cardDetails: LiveData<CardDetail>
        val openCardDetailsBottomSheet: LiveData<SingleEvent<Boolean>>
        val isFreezeCardConfigUpdated: LiveData<Boolean>
        fun getCardBalance(card: Card): String
        fun getCardState(card: Card, userAccount: AccountInfo?)
        fun fetchCardDetails(cardSerialNumber: String)
        fun openCardDetailsBottomSheet()
        fun updateCardFreezeConfig(cardSerialNumber: String)
        fun getCardName(card: Card): String

    }


    interface State : IBase.State
}