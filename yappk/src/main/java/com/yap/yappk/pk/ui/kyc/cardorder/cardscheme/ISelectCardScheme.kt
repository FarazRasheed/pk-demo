package com.yap.yappk.pk.ui.kyc.cardorder.cardscheme

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.pk.ui.kyc.cardorder.cardscheme.cardschemelist.CardSchemeListAdapter

interface ISelectCardScheme {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val cardSchemes: LiveData<List<CardScheme>>
        val multiStateView: LiveData<MultiState>
        val cardSchemeAdapter: CardSchemeListAdapter
        fun getCardSchemes()
        fun getReloadListener(): MultiStateLayout.OnReloadListener
    }

    interface State : IBase.State
}