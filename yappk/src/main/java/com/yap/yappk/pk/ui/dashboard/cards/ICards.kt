package com.yap.yappk.pk.ui.dashboard.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.listadapter.CardListAdapter
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapter

interface ICards {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var cardsAdapter: CardPagerAdapter
        val card: LiveData<SingleEvent<Card?>>
        val cardList: LiveData<List<Card>>
        val cardListAdapter: CardListAdapter
        val multiStateView: LiveData<MultiState>
        val sessionManager: SessionManager
        var pagerCurrentPosition: Int
        fun onCardListCount(count: Int)
        fun setCard(card: Card?)
        fun setCardList(cardList: MutableList<Card>)
        fun getCardName(card: Card): String
        fun getPageCountTitle(cardPosition: Int, totalNoOfCards: Int): String
        fun sortByType(cardList: List<Card>): ArrayList<Card>
        fun getCardState(card: Card, userAccount: AccountInfo?, callBack: (CardState?) -> Unit)
    }

    interface State : IBase.State {
        val isListView: MutableLiveData<Boolean>
        var isAccountCreated: MutableLiveData<Boolean>
    }
}