package com.yap.yappk.pk.ui.kyc.cvv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager

interface IOnBoardingCardCvv {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        fun setCard(card: ExternalCard?)
        fun setTopUpAmount(amount: String?)
        fun setSecureId(id: String?)
        fun setOrderId(orderId: String?)
        val externalCard: LiveData<ExternalCard?>
        val topUpAmount: LiveData<String?>
        val secureId: LiveData<String?>
        val orderId: LiveData<String?>
    }

    interface State : IBase.State {
        var cvv: MutableLiveData<String>
    }
}