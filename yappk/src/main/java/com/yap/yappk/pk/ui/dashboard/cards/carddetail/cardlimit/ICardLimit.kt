package com.yap.yappk.pk.ui.dashboard.cards.carddetail.cardlimit

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase

interface ICardLimit {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val isAtmConfigUpdated: LiveData<Boolean>
        val isRetailConfigUpdated: LiveData<Boolean>
        fun updateAtmConfig(cardSerialNumber: String)
        fun updateRetailConfig(cardSerialNumber: String)
    }

    interface State : IBase.State
}