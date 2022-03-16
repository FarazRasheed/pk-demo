package com.yap.yappk.pk.ui.dashboard.main

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.deeplink.IDeeplinkNavigator
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface IDashboard {
    interface View : IBase.View<ViewModel> {
        fun openAddMoneyScreen()
        fun viewModelObservers()
        fun openSendMoneyScreen()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var animClosed: Boolean
        val debitCard: LiveData<Card>
        val userCards: LiveData<MutableList<Card>>
        val closeOverly: LiveData<Boolean>
        val deeplinkHandler: IDeeplinkNavigator
        fun fetchDebitCard()
        fun fetchCards()
        fun isOverlyShowing(): Boolean
        fun closeOverly()
    }

    interface State : IBase.State
}