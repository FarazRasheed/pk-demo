package com.yap.yappk.pk.ui.dashboard.cards.enums

import com.yap.yappk.networking.microservices.cards.responsedtos.Card

sealed class CardSettings {
    data class ChangeCardName(val card: Card?) : CardSettings()
    data class ChangeCardPin(val card: Card?) : CardSettings()
    data class ForgotCardPin(val card: Card?) : CardSettings()
    data class ViewCardStatement(val card: Card?) : CardSettings()
    data class ReportOrStolenCard(val card: Card?) : CardSettings()
    object None : CardSettings()
}
