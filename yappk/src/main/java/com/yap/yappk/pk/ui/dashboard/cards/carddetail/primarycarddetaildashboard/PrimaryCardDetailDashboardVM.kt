package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionlist.CardTransactionListAdapter
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardSettings
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getFormattedDate
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardDetailUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardFreezeUnfreezeUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardTransactionUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.utils.pagination.PaginatedRecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrimaryCardDetailDashboardVM @Inject constructor(
    override val viewState: PrimaryCardDetailDashboardState,
    private val resourcesProvider: ResourcesProviders,
    override val sessionManager: SessionManager,
    private val cardFreezeUnfreeze: CardFreezeUnfreezeUC,
    private val cardDetailUC: CardDetailUC,
    private val cardTransactionUC: CardTransactionUC
) : BaseViewModel<IPrimaryCardDetailDashboard.State>(), IPrimaryCardDetailDashboard.ViewModel {

    private val _openCardDetailMoreBottomSheet: MutableLiveData<SingleEvent<Boolean>> =
        MutableLiveData()
    override val openCardDetailMoreBottomSheet: LiveData<SingleEvent<Boolean>> =
        _openCardDetailMoreBottomSheet

    private val _isFreezeCardConfigUpdated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    override val isFreezeCardConfigUpdated: LiveData<Boolean> = _isFreezeCardConfigUpdated

    private val _cardDetails: MutableLiveData<CardDetail> = MutableLiveData()
    override val cardDetails: LiveData<CardDetail> = _cardDetails

    private val _multiStateView: MutableLiveData<MultiState> = MutableLiveData()
    override val multiStateView: LiveData<MultiState> = _multiStateView

    private val _cardTransaction: MutableLiveData<List<Transaction>> = MutableLiveData()
    override val cardTransaction: LiveData<List<Transaction>> = _cardTransaction

    private val _cardTotalTransaction: MutableLiveData<List<Transaction>> = MutableLiveData()
    override val cardTotalTransaction: LiveData<List<Transaction>> = _cardTotalTransaction

    private val _isTransactionsCompleted: MutableLiveData<Boolean> = MutableLiveData()
    override val isTransactionsCompleted: LiveData<Boolean> = _isTransactionsCompleted

    private val _isTransactionsSuccess: MutableLiveData<Boolean> = MutableLiveData()
    override val isTransactionsSuccess: LiveData<Boolean> = _isTransactionsSuccess

    override val cardTransactionListAdapter: CardTransactionListAdapter =
        CardTransactionListAdapter(mutableListOf(), resourcesProvider)

    override var mCardSerialNumber: String? = null
    override var mPageNumber: Int = 0
    override var mPageSize: Int = 10
    override var isFirstTimeTransactionFetching: Boolean = true
    override var filterData: FilterExtras? = null

    override fun openCardDetailMoreBottomSheet() {
        _openCardDetailMoreBottomSheet.value = SingleEvent(true)
    }

    override fun getMoreOptionList(): ArrayList<String> {
        val optionList: ArrayList<String> = arrayListOf()
        optionList.add(resourcesProvider.getString(screen_card_detail_display_text_name_your_card))
        optionList.add(resourcesProvider.getString(screen_card_detail_display_text_change_pin))
        optionList.add(resourcesProvider.getString(screen_current_card_pin_display_text_forgot_pin))
        optionList.add(resourcesProvider.getString(screen_card_detail_display_text_view_statement))
        optionList.add(resourcesProvider.getString(screen_card_detail_display_text_report))
        return optionList
    }

    override fun getCardName(card: Card): String {
        return when (card.cardType) {
            CardType.DEBIT.name -> {
                if (card.nameUpdated == true)
                    card.cardName ?: ""
                else
                    resourcesProvider.getString(screen_card_status_display_text_title)
            }
            CardType.PREPAID.name -> {
                card.cardName ?: ""
            }
            else -> {
                throw IllegalStateException("Invalid card type found " + card.cardType)
            }
        }
    }

    override fun onCardListCount(count: Int) {
        _multiStateView.value = if (count == 0) MultiState.empty(null) else MultiState.success(null)
    }

    override fun getCardTypeText(card: Card): String {
        return when (card.cardType) {
            CardType.DEBIT.name -> {
                resourcesProvider.getString(screen_card_status_display_text_title)
            }
            CardType.PREPAID.name -> {
                resourcesProvider.getString(screen_card_status_display_text_virtual)
            }
            else -> {
                throw IllegalStateException("Invalid card type found " + card.cardType)
            }
        }
    }

    override fun getCardBalance(card: Card): String {
        return sessionManager.userAccount.value?.currency?.code + " " + (card.cardBalance ?: "0.00")
    }

    override fun getCardFreezeText(isCardFreeze: Boolean): String {
        return if (isCardFreeze)
            resourcesProvider.getString(screen_card_detail_display_text_Unfreeze)
        else
            resourcesProvider.getString(screen_card_detail_display_text_freeze)
    }

    override fun updateCardFreezeConfig(cardSerialNumber: String) {
        showLoading()
        cardFreezeUnfreeze.executeUseCase(CardFreezeUnfreezeUC.RequestValues(cardSerialNumber),
            object :
                UseCaseCallback<CardFreezeUnfreezeUC.ResponseValue, CardFreezeUnfreezeUC.ErrorValue> {
                override fun onSuccess(response: CardFreezeUnfreezeUC.ResponseValue) {
                    hideLoading()
                    _isFreezeCardConfigUpdated.value = true
                }

                override fun onError(error: CardFreezeUnfreezeUC.ErrorValue) {
                    hideLoading()
                    _isFreezeCardConfigUpdated.value = false
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun fetchCardDetails(cardSerialNumber: String) {
        cardDetailUC.executeUseCase(
            CardDetailUC.RequestValues(cardSerialNumber),
            object : UseCaseCallback<CardDetailUC.ResponseValue, CardDetailUC.ErrorValue> {
                override fun onSuccess(response: CardDetailUC.ResponseValue) {
                    if (response.cardDetail != null) {
                        response.cardDetail.state = StateEnum.CONTENT
                        _cardDetails.value = response.cardDetail
                    } else {
                        _cardDetails.value = CardDetail(state = StateEnum.EMPTY)
                    }
                }

                override fun onError(error: CardDetailUC.ErrorValue) {
                    _cardDetails.value = CardDetail(state = StateEnum.ERROR)
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun fetchCardTransactions(
        cardSerialNumber: String,
        pageSize: Int,
        pageNumber: Int,
        filterExtras: FilterExtras?
    ) {
        if (isFirstTimeTransactionFetching) showLoading()
        cardTransactionUC.executeUseCase(
            CardTransactionUC.RequestValues(
                cardSerialNumber = cardSerialNumber,
                pageSize = pageSize,
                pageNumber = pageNumber,
                minRange = filterExtras?.minRange?.toDouble(),
                maxRange = filterExtras?.maxRange?.toDouble(),
                txnTyp = filterExtras?.txnType
            ),
            object :
                UseCaseCallback<CardTransactionUC.ResponseValue, CardTransactionUC.ErrorValue> {
                override fun onSuccess(response: CardTransactionUC.ResponseValue) {
                    _cardTransaction.value = response.transactionList
                    _isTransactionsCompleted.value = response.isLast
                    _isTransactionsSuccess.value = true
                    if (isFirstTimeTransactionFetching || (response.transactionList.isNullOrEmpty() && pageNumber == 0)) _multiStateView.value =
                        MultiState.empty()
                    else
                        _multiStateView.value = MultiState.success()
                    isFirstTimeTransactionFetching = false
                    hideLoading()
                }

                override fun onError(error: CardTransactionUC.ErrorValue) {
                    _isTransactionsSuccess.value = false
                    if (isFirstTimeTransactionFetching) _multiStateView.value =
                        MultiState.error()
                    hideLoading()
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun getTargetScreen(position: Int, card: Card?): CardSettings {
        return when (position) {
            0 -> CardSettings.ChangeCardName(card)
            1 -> CardSettings.ChangeCardPin(card)
            2 -> CardSettings.ForgotCardPin(card)
            3 -> CardSettings.ViewCardStatement(card)
            4 -> CardSettings.ReportOrStolenCard(card)
            else -> CardSettings.None
        }
    }

    private val paginationListener = object : PaginatedRecyclerView.Pagination() {
        override fun onNextPage(page: Int) {
            mPageNumber = page
            fetchCardTransactions(
                cardSerialNumber = mCardSerialNumber ?: "",
                pageSize = mPageSize,
                pageNumber = mPageNumber,
                filterExtras = filterData
            )
        }
    }

    override fun getPaginationListener(): PaginatedRecyclerView.Pagination {
        return paginationListener
    }

    override fun sortByDate(
        pageNumber: Int,
        oldTransactionList: List<Transaction>,
        newTransactionList: List<Transaction>
    ) {
        val transactionList: ArrayList<Transaction> = arrayListOf()
        if (pageNumber != 0)
            transactionList.addAll(oldTransactionList)
        transactionList.addAll(newTransactionList)
        transactionList.forEach { transaction ->
            transaction.type = 0
        }
        val transactionGroupByDate =
            transactionList.sortedByDescending { it.creationDateTime }.groupBy { item ->
                item.getFormattedDate()
            }
        _cardTotalTransaction.value = appendSections(transactionGroupByDate)
    }


    private fun appendSections(transactionItemList: Map<String?, List<Transaction>>): ArrayList<Transaction> {
        val finalList = ArrayList<Transaction>()
        transactionItemList.forEach { transcation ->
            transcation.value.first().type = 1
            transcation.value.first().totalAmount =
                sessionManager.userAccount.value?.currency?.code + " " + getTotalAmount(transcation)
            finalList.addAll(transcation.value)
        }
        return finalList
    }

    private fun getTotalAmount(transcation: Map.Entry<String?, List<Transaction>>): String {
        var totalAmount: Double = 0.0
        transcation.value.forEach { transactionValue ->
            totalAmount = totalAmount.plus(transactionValue.amount?.toDouble() ?: 0.0)
        }
        return totalAmount.toString()
    }

    private val rvReloadListener = object : MultiStateLayout.OnReloadListener {
        override fun onReload(view: View) {
            fetchCardTransactions(
                cardSerialNumber = mCardSerialNumber ?: "",
                pageSize = mPageSize,
                pageNumber = mPageNumber,
                filterExtras = filterData
            )
        }
    }

    override fun getReloadListener(): MultiStateLayout.OnReloadListener {
        return rvReloadListener
    }

}