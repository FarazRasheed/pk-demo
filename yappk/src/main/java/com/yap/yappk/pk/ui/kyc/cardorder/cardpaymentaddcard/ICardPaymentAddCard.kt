package com.yap.yappk.pk.ui.kyc.cardorder.cardpaymentaddcard

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.configurations.PKBuildConfigurations

interface ICardPaymentAddCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun getAddCardPageUrl(buildFlavour: String): String
        fun addExternalCard(
            sessionId: String,
            cardAlias: String,
            cardColor: String,
            cardNumber: String
        )

        fun setExternalCard(
            sessionId: String,
            cardAlias: String,
            cardColor: String,
            cardNumber: String
        )

        fun openAddressScreen()

        val openAddressConfirmation: LiveData<SingleEvent<Int>>
        val card: LiveData<ExternalCard>
        val pkBuildConfigurations: PKBuildConfigurations
    }

    interface State : IBase.State
}