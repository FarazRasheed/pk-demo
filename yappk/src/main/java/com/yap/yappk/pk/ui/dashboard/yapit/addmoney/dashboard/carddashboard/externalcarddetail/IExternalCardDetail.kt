package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarddetail

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard

interface IExternalCardDetail {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val card: LiveData<ExternalCard?>
        val isCardDeleted: LiveData<Boolean>
        fun setCard(card: ExternalCard?)
        fun deleteExternalCard(cardId: String)
    }

    interface State : IBase.State
}