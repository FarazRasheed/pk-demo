package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter

import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard

interface ExternalCardPagerListener {
    fun openAddCardScreen()
    fun openCardDetailScreen()
    fun openExternalCardTopUp(externalCard: ExternalCard?)
}