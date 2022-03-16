package com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.cards.responsedtos.CardSchemeBenefit
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits.cardschemebenefitlist.CardSchemeBenefitListAdapter
import com.yap.yappk.pk.ui.kyc.cardorder.cardscheme.cardschemelist.CardSchemeListAdapter

interface ICardSchemeBenefits {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val cardScheme: LiveData<SingleEvent<CardScheme>>
        val cardSchemeBenefits: LiveData<List<CardSchemeBenefit>>
        val multiStateView: LiveData<MultiState>
        val cardSchemeBenefitAdapter: CardSchemeBenefitListAdapter
        fun setCardScheme(cardScheme: CardScheme?)
        fun getCardSchemeBenefits(cardScheme: String)
        fun getReloadListener(): MultiStateLayout.OnReloadListener
    }

    interface State : IBase.State
}