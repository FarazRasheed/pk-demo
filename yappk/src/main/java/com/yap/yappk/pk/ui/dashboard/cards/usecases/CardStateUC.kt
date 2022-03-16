package com.yap.yappk.pk.ui.dashboard.cards.usecases

import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.ui.dashboard.cards.enums.*
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.BaseUseCase
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.utils.enums.PKPartnerBankStatus
import javax.inject.Inject

class CardStateUC @Inject constructor() : BaseUseCase<CardStateUC.RequestValues,
        CardStateUC.ResponseValue,
        CardStateUC.ErrorValue>() {

    private fun getCardState(card: Card, userAccount: AccountInfo?): CardState? {
        return when {
            card.cardType == CardType.DEBIT.name
                    && (PKPartnerBankStatus.SIGN_UP_PENDING.name == userAccount?.partnerBankStatus
                    || PKPartnerBankStatus.IBAN_ASSIGNED.name == userAccount?.partnerBankStatus) -> CardState.NOT_ORDERED

            card.cardType == CardType.DEBIT.name
                    && (PKPartnerBankStatus.ACTIVATED.name == userAccount?.partnerBankStatus || PKPartnerBankStatus.PHYSICAL_CARD_SUCCESS.name == userAccount?.partnerBankStatus)
                    && (card.status == CardStatus.ACTIVE.name || card.status == CardStatus.INACTIVE.name)
                    && (card.deliveryStatus != CardDeliveryStatus.SHIPPED.name && card.deliveryStatus != CardDeliveryStatus.FAILED.name)
                    && card.pinCreated == false -> CardState.DELIVERY_IN_PROGRESS

            card.cardType == CardType.DEBIT.name
                    && (PKPartnerBankStatus.ACTIVATED.name == userAccount?.partnerBankStatus || PKPartnerBankStatus.PHYSICAL_CARD_SUCCESS.name == userAccount?.partnerBankStatus)
                    && (card.status == CardStatus.ACTIVE.name || card.status == CardStatus.INACTIVE.name)
                    && card.deliveryStatus == CardDeliveryStatus.SHIPPED.name
                    && card.pinCreated == false -> CardState.SET_PIN_REQUIRED

            card.cardType == CardType.DEBIT.name
                    && card.status == CardStatus.ACTIVE.name
                    && card.deliveryStatus == CardDeliveryStatus.SHIPPED.name
                    && card.pinStatus == CardPinStatus.ACTIVE.name
                    && card.pinCreated == true -> CardState.ACTIVATED

            card.cardType == CardType.DEBIT.name && card.status == CardStatus.HOTLISTED.name -> CardState.RE_ORDER_REQUIRED

            card.status == CardStatus.BLOCKED.name -> CardState.FREEZE

            card.status == CardStatus.EXPIRED.name -> CardState.EXPIRED

            else -> {
                null
            }
        }
    }

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        val cardStat = getCardState(requestValues?.card ?: Card(), requestValues?.userAccount)
        cardStat?.let { cardState ->
            responseCallback?.onSuccess(ResponseValue(cardState))
        } ?: responseCallback?.onError(ErrorValue())
    }

    class RequestValues(val card: Card, val userAccount: AccountInfo?) : BaseUseCase.RequestValues
    class ResponseValue(val cardState: CardState?) : BaseUseCase.ResponseValue
    class ErrorValue : ResponseError

}
