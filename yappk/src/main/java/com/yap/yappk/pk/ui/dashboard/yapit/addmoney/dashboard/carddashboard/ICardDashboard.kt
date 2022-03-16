package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerAdapter

interface ICardDashboard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val externalCards: LiveData<List<ExternalCard>>
        var cardsAdapter: ExternalCardPagerAdapter?
        val card: LiveData<SingleEvent<ExternalCard?>>
        var pagerCurrentPosition: Int
        fun getExternalCards()
        fun setCard(card: ExternalCard?)
    }

    interface State : IBase.State {
        val tbTitle: MutableLiveData<String>
    }
}