package com.yap.yappk.pk.ui.dashboard.cards.extensions

import com.yap.yappk.R
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.kyc.enums.CardSchemeEnum

fun Card.getCardImageResource(): Int {
    return when (this.cardScheme) {
        CardSchemeEnum.PayPak.scheme -> {
            R.drawable.pk_spare_card_paypak
        }
        else -> {
            R.drawable.pk_card_spare
        }
    }
}