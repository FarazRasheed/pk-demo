package com.yap.yappk.pk.ui.kyc.paymentconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.pk.SessionManager

interface ICardOrderPaymentConfirmation {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun setAddress(address: CardOrderRequest?)
        fun createTopUpSessionUseCase(
            sessionId: String? = null,
            beneficiaryId: Int?,
            currency: String,
            amount: String?
        )

        fun setSecureId(secureId: String?)
        fun setOrderId(orderId: String?)
        fun startPooling(secureId: String?)
        fun topUpTransactionRequest(
            beneficiaryId: String?,
            secureId: String,
            orderId: String,
            currency: String,
            amount: String,
            securityCode: String?,
            sessionId: String?
        )

        fun openCardOrderSuccessScreen()
        fun openValidCustomerScreen()
        fun openInValidCustomerScreen()
        fun saveAddressCard(cardOrderRequest: CardOrderRequest)
        fun orderCard(
            cardFee: String,
            cardScheme: String
        )

        val secureId: LiveData<String?>
        val orderId: LiveData<String?>
        val address: LiveData<CardOrderRequest>
        val check3dSecureSuccess: LiveData<Check3DEnrollmentSession?>
        val sessionManager: SessionManager
        val poolingSuccess: LiveData<Boolean>
        val topUpSuccess: LiveData<Boolean>
        val openCardOrderSuccess: LiveData<SingleEvent<Int>>
        val openValidCustomer: LiveData<SingleEvent<Int>>
        val openInValidCustomer: LiveData<SingleEvent<Int>>
        val accountInfo: LiveData<AccountInfo?>
        val isCardOrdered: LiveData<AccountInfo?>

    }

    interface State : IBase.State {
        var orderNewCardFee: MutableLiveData<String>
        var cardAddressTitle: MutableLiveData<String>?
        var cardAddressSubTitle: MutableLiveData<String>?
    }
}
