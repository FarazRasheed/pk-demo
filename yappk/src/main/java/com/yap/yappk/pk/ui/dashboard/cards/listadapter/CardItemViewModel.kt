package com.yap.yappk.pk.ui.dashboard.cards.listadapter

import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

class CardItemViewModel(
    val cardData: Card,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
)