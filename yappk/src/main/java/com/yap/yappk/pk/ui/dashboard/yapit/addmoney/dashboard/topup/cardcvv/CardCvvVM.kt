package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.cardcvv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardCvvVM @Inject constructor(
    override val viewState: CardCvvState,
    private val transactionsApi: TransactionsApi,
    override val sessionManager: SessionManager
) :
    BaseViewModel<ICardCvv.State>(), ICardCvv.ViewModel {
    private val _topUpSuccess = MutableLiveData<Boolean>()
    override val topUpSuccess: LiveData<Boolean> = _topUpSuccess
    private val _externalCard: MutableLiveData<ExternalCard?> = MutableLiveData()
    override val externalCard: LiveData<ExternalCard?> = _externalCard
    private val _topUpAmount: MutableLiveData<String?> = MutableLiveData()
    override val topUpAmount: LiveData<String?> = _topUpAmount

    private val _secureId: MutableLiveData<String?> = MutableLiveData()
    override val secureId: LiveData<String?> = _secureId
    private val _orderId: MutableLiveData<String?> = MutableLiveData()
    override val orderId: LiveData<String?> = _orderId
    override fun setCard(card: ExternalCard?) {
        _externalCard.value = card
    }

    override fun setTopUpAmount(amount: String?) {
        _topUpAmount.value = amount
    }

    override fun setSecureId(id: String?) {
        _secureId.value = id
    }

    override fun setOrderId(orderId: String?) {
        _orderId.value = orderId
    }

    override fun topUpTransactionRequest(
        beneficiaryId: String,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String
    ) {
        showLoading()
        launch {
            val response =
                transactionsApi.cardTopUpTransactionRequest(
                    beneficiaryId,
                    secureId,
                    orderId,
                    currency,
                    amount,
                    securityCode
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _topUpSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        showToast(response.error.message)
                    }
                }
                hideLoading()
            }
        }
    }
}

