package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionlist

import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction

class CardTransactionItemViewModel(
    val cardData: Transaction,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
)