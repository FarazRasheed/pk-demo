package com.yap.yappk.pk.ui.dashboard.cards.enums

import androidx.annotation.Keep

@Keep
enum class CardDeliveryStatus {
    ORDERED,
    BOOKED,
    SHIPPING,
    SHIPPED,
    FAILED;
}