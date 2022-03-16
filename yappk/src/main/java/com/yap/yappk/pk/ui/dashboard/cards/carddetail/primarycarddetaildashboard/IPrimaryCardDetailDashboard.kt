package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionlist.CardTransactionListAdapter
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardSettings
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import com.yap.yappk.pk.utils.pagination.PaginatedRecyclerView

interface IPrimaryCardDetailDashboard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val openCardDetailMoreBottomSheet: LiveData<SingleEvent<Boolean>>
        val isFreezeCardConfigUpdated: LiveData<Boolean>
        val cardDetails: LiveData<CardDetail>
        val sessionManager: SessionManager
        val cardTransactionListAdapter: CardTransactionListAdapter
        val cardTransaction: LiveData<List<Transaction>>
        val cardTotalTransaction: LiveData<List<Transaction>>
        val multiStateView: LiveData<MultiState>
        val isTransactionsCompleted: LiveData<Boolean>
        val isTransactionsSuccess: LiveData<Boolean>
        var mCardSerialNumber: String?
        var mPageNumber: Int
        var mPageSize: Int
        var isFirstTimeTransactionFetching: Boolean
        var filterData: FilterExtras?
        fun openCardDetailMoreBottomSheet()
        fun getMoreOptionList(): ArrayList<String>
        fun getCardName(card: Card): String
        fun getCardTypeText(card: Card): String
        fun getCardBalance(card: Card): String
        fun getCardFreezeText(isCardFreeze: Boolean): String
        fun updateCardFreezeConfig(cardSerialNumber: String)
        fun fetchCardDetails(cardSerialNumber: String)
        fun fetchCardTransactions(
            cardSerialNumber: String,
            pageSize: Int,
            pageNumber: Int,
            filterExtras: FilterExtras?
        )

        fun getTargetScreen(position: Int, card: Card?): CardSettings
        fun onCardListCount(count: Int)
        fun getPaginationListener(): PaginatedRecyclerView.Pagination
        fun sortByDate(
            pageNumber: Int,
            oldTransactionList: List<Transaction>,
            newTransactionList: List<Transaction>
        )

        fun getReloadListener(): MultiStateLayout.OnReloadListener
    }

    interface State : IBase.State {
        val cardName: MutableLiveData<String>
        val cardType: MutableLiveData<String>
        val cardMaskNo: MutableLiveData<String>
        val cardBalance: MutableLiveData<String>
        val cardFreezeText: MutableLiveData<String>
        val isCardFreezes: MutableLiveData<Boolean>
    }
}