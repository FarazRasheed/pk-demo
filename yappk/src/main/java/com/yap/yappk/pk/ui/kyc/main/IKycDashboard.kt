package com.yap.yappk.pk.ui.kyc.main

import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard

interface IKycDashboard {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var citizenNumber: String
        val paths: ArrayList<String>
        var isFromOnboarding: Boolean
        var cardScheme: CardScheme?
        var externalCard: ExternalCard?
        var cardDeliveryAddress: CardOrderRequest?
        var toolbarVisibilityGone: MutableLiveData<Boolean>
        fun setProgress(percent: Int)
        fun setProgressVisibility(visible: Boolean)
    }

    interface State : IBase.State {
        var totalProgress: ObservableInt
        var currentProgress: ObservableInt
    }
}