package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.configurations.PKBuildConfigurations

interface IAddExternalCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun getAddCardPageUrl(buildFlavour: String): String
        fun addExternalCard(
            sessionId: String,
            cardAlias: String,
            cardColor: String,
            cardNumber: String
        )
        val card: LiveData<ExternalCard>
        val pkBuildConfigurations: PKBuildConfigurations
    }

    interface State : IBase.State
}