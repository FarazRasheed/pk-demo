package com.yap.yappk.pk.ui.dashboard.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.yappk.localization.screen_card_section_display_text_card_page_count_text
import com.yap.yappk.localization.screen_card_status_display_text_title
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.listadapter.CardListAdapter
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapter
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardStateUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardsVM @Inject constructor(
    override val viewState: CardsState,
    private val resourcesProvider: ResourcesProviders,
    override val sessionManager: SessionManager,
    private val cardStateUC: CardStateUC
) : BaseViewModel<ICards.State>(), ICards.ViewModel {
    override lateinit var cardsAdapter: CardPagerAdapter

    private val _card: MutableLiveData<SingleEvent<Card?>> = MutableLiveData()
    override val card: LiveData<SingleEvent<Card?>> = _card

    private val _cardList: MutableLiveData<List<Card>> = MutableLiveData()
    override val cardList: LiveData<List<Card>> = _cardList

    private val _multiStateView: MutableLiveData<MultiState> = MutableLiveData()
    override val multiStateView: LiveData<MultiState> = _multiStateView
    override var pagerCurrentPosition: Int = -1

    override val cardListAdapter: CardListAdapter = CardListAdapter(arrayListOf())

    override fun setCard(card: Card?) {
        _card.value = SingleEvent(card)
    }

    override fun setCardList(cardList: MutableList<Card>) {
        val debitCard = cardList.find { card -> card.cardType == CardType.DEBIT.name }
        if (debitCard != null) {
            cardList.find { card -> card.currency == null }?.currency =
                sessionManager.userAccount.value?.currency?.code
            _cardList.value = cardList
        } else {
            val list: ArrayList<Card> = arrayListOf()
            list.add(
                Card(
                    cardType = CardType.DEBIT.name,
                    currency = (sessionManager.userAccount.value?.currency?.code ?: "PKR")
                )
            )
            list.addAll(cardList)
            _cardList.value = list
        }
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

    override fun getPageCountTitle(cardPosition: Int, totalNoOfCards: Int): String {
        return resourcesProvider.getString(
            screen_card_section_display_text_card_page_count_text,
            (cardPosition + 1).toString(),
            totalNoOfCards.toString()
        )
    }

    override fun onCardListCount(count: Int) {
        _multiStateView.value = if (count == 0) MultiState.empty(null) else MultiState.success(null)
    }

    override fun sortByType(cardList: List<Card>): ArrayList<Card> {
        val carGroupByType =
            cardList.sortedByDescending { it.cardType == CardType.DEBIT.name }.groupBy { card ->
                card.cardType
            }
        return appendSections(carGroupByType)
    }

    override fun getCardState(
        card: Card,
        userAccount: AccountInfo?,
        callBack: (CardState?) -> Unit
    ) {
        cardStateUC.executeUseCase(CardStateUC.RequestValues(card, userAccount), object :
            UseCaseCallback<CardStateUC.ResponseValue, CardStateUC.ErrorValue> {
            override fun onSuccess(response: CardStateUC.ResponseValue) {
                callBack.invoke(response.cardState)
            }

            override fun onError(error: CardStateUC.ErrorValue) {
                callBack.invoke(null)
            }
        })
    }

    private fun appendSections(cardItemList: Map<String?, List<Card>>): ArrayList<Card> {
        val finalList = ArrayList<Card>()
        cardItemList.forEach { card ->
            card.value.first().type = 1
            finalList.addAll(card.value)
        }
        return finalList
    }
}