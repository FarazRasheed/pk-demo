package com.yap.yappk.pk.ui.dashboard.cards.cardstatus

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.adapter.StatusDataModel

interface ICardStatus {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun handleOrderedStatus(): ArrayList<StatusDataModel>
        fun handleBookedStatus(): ArrayList<StatusDataModel>
        fun handleShippingStatus(): ArrayList<StatusDataModel>
        fun handleShippedStatus(): ArrayList<StatusDataModel>
        fun handleStatus(): ArrayList<StatusDataModel>
    }

    interface State : IBase.State {
        var valid: MutableLiveData<Boolean>
    }
}