package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.cardcvv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager

interface ICardCvv {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val topUpSuccess: LiveData<Boolean>
        fun setCard(card: ExternalCard?)
        fun setTopUpAmount(amount: String?)
        fun setSecureId(id: String?)
        fun setOrderId(orderId: String?)
        fun topUpTransactionRequest(
            beneficiaryId: String,
            secureId: String,
            orderId: String,
            currency: String,
            amount: String,
            securityCode: String
        )

        val externalCard: LiveData<ExternalCard?>
        val topUpAmount: LiveData<String?>
        val secureId: LiveData<String?>
        val orderId: LiveData<String?>
    }

    interface State : IBase.State {
        var cvv: MutableLiveData<String>
    }
}