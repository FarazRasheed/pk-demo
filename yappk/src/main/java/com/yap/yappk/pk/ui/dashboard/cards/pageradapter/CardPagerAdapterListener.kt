package com.yap.yappk.pk.ui.dashboard.cards.pageradapter

import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface CardPagerAdapterListener {
    fun openAddCardScreen()
    fun openCardDetailScreen(card: Card?)
    fun openCardStatusScreen(debitCard: Card?)
    fun openCardPinScreen(debitCard: Card?)
    fun updateCardPagerItem(card: Card?, position: Int)
    fun cardReorderFlow(card: Card?)
}