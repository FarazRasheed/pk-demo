package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.cards.responsedtos.Card

interface IReportCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun toggleReportLostAndDamageCardOptions(toggle: Boolean)
        fun getHelpDeskNumber()
        fun requestConfirmBlockCard(card: Card?)
        val reportCardSuccess: LiveData<Boolean>
        val openReorderFlow: LiveData<SingleEvent<Int>>
        var HOT_LIST_REASON: Int
    }

    interface State : IBase.State {
        var isCardDamageOption: MutableLiveData<Boolean>
        var helpDeskNumber: MutableLiveData<String>?
    }
}
