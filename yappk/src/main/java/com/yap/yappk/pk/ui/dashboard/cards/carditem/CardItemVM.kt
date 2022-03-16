package com.yap.yappk.pk.ui.dashboard.cards.carditem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.yappk.localization.screen_card_section_display_text_title_detail
import com.yap.yappk.localization.screen_card_status_display_text_title
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardDetailUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardFreezeUnfreezeUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardStateUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardItemVM @Inject constructor(
    override val viewState: CardItemState,
    private val resourcesProvider: ResourcesProviders,
    override val sessionManager: SessionManager,
    private val cardStateUC: CardStateUC,
    private val cardFreezeUnfreeze: CardFreezeUnfreezeUC,
    private val cardDetailUC: CardDetailUC
) : BaseViewModel<ICardItem.State>(), ICardItem.ViewModel {

    private val _cardState: MutableLiveData<CardState> = MutableLiveData()
    override val cardState: LiveData<CardState> = _cardState

    private val _openCardDetailsBottomSheet: MutableLiveData<SingleEvent<Boolean>> =
        MutableLiveData()
    override val openCardDetailsBottomSheet: LiveData<SingleEvent<Boolean>> =
        _openCardDetailsBottomSheet

    private val _cardDetails: MutableLiveData<CardDetail> = MutableLiveData()
    override val cardDetails: LiveData<CardDetail> = _cardDetails

    private val _isFreezeCardConfigUpdated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    override val isFreezeCardConfigUpdated: LiveData<Boolean> = _isFreezeCardConfigUpdated

    override fun getCardState(card: Card, userAccount: AccountInfo?) {
        cardStateUC.executeUseCase(CardStateUC.RequestValues(card, userAccount), object :
            UseCaseCallback<CardStateUC.ResponseValue, CardStateUC.ErrorValue> {
            override fun onSuccess(response: CardStateUC.ResponseValue) {
                _cardState.value = response.cardState
            }

            override fun onError(error: CardStateUC.ErrorValue) {
                _cardState.value = null
            }

        })

    }

    override fun fetchCardDetails(cardSerialNumber: String) {
        cardDetailUC.executeUseCase(
            CardDetailUC.RequestValues(cardSerialNumber),
            object :
                UseCaseCallback<CardDetailUC.ResponseValue, CardDetailUC.ErrorValue> {
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

    override fun getCardName(card: Card): String {
        return when (card.cardType) {
            CardType.DEBIT.name -> {
                if (card.nameUpdated == true)
                    resourcesProvider.getString(
                        screen_card_section_display_text_title_detail,
                        card.cardName ?: ""
                    )
                else
                    resourcesProvider.getString(
                        screen_card_section_display_text_title_detail,
                        resourcesProvider.getString(screen_card_status_display_text_title)
                    )
            }
            CardType.PREPAID.name -> {
                resourcesProvider.getString(
                    screen_card_section_display_text_title_detail,
                    card.cardName ?: ""
                )
            }
            else -> {
                throw IllegalStateException("Invalid card type found " + card.cardType)
            }
        }
    }

    override fun openCardDetailsBottomSheet() {
        _openCardDetailsBottomSheet.value = SingleEvent(true)
    }

    override fun getCardBalance(card: Card): String {
        return sessionManager.userAccount.value?.currency?.code + " " + (card.cardBalance ?: "0.00")
    }


}