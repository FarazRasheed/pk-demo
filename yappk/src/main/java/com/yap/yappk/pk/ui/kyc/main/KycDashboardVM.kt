package com.yap.yappk.pk.ui.kyc.main

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KycDashboardVM @Inject constructor(override val viewState: KycDashboardState) :
    BaseViewModel<IKycDashboard.State>(), IKycDashboard.ViewModel {
    override var citizenNumber: String = ""
    override val paths: ArrayList<String> = arrayListOf()
    override var isFromOnboarding: Boolean = false
    override var cardScheme: CardScheme? = null
    override var externalCard: ExternalCard? = null
    override var cardDeliveryAddress: CardOrderRequest? = null
    override var toolbarVisibilityGone: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun setProgress(percent: Int) {
        viewState.currentProgress.set(percent)
    }

    override fun setProgressVisibility(visible: Boolean) {
        viewState.toolsBarVisibility.set(visible)
    }
}