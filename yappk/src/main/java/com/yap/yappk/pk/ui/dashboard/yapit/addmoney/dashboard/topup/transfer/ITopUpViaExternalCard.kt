package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.adapter.DenominationsAdapter

interface ITopUpViaExternalCard {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val externalCard: LiveData<ExternalCard?>
        val secureId: LiveData<String?>
        val orderId: LiveData<String?>
        var transactionThreshold: MutableLiveData<TransactionThresholdResponse>?
        val denominationsAdapter: DenominationsAdapter
        val errorDescription: LiveData<CharSequence>
        val checkOutSessionResponse: LiveData<CreateSession>
        val check3dSecureSuccess: LiveData<Check3DEnrollmentSession>
        val poolingSuccess: LiveData<Boolean>
        fun setCard(card: ExternalCard?)
        fun setSecureId(secureId: String?)
        fun setOrderId(orderId: String?)
        fun fetchAllInitialApis()
        fun callApiUseCase()
        fun onAmountChange(amount: CharSequence, start: Int, before: Int, count: Int)
        fun createTransactionSession(currency: String?, amount: String?)
        fun check3DEnrollmentSessionRequest(
            beneficiaryId: Int?,
            orderId: String?,
            sessionId: String?,
            currency: String?,
            amount: String?
        )

        fun startPooling(secureId: String?)
    }

    interface State : IBase.State {
        var transferFee: MutableLiveData<String>
        var minLimit: MutableLiveData<Double>?
        var maxLimit: MutableLiveData<Double>?
        var accountCurrentBalance: MutableLiveData<String>?
        var isFocusable: MutableLiveData<Boolean>
        var isValidAmount: MutableLiveData<Boolean>
    }
}