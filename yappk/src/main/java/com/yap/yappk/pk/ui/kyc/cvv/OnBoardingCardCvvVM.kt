package com.yap.yappk.pk.ui.kyc.cvv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingCardCvvVM @Inject constructor(
    override val viewState: OnBoardingCardCvvState,
    override val sessionManager: SessionManager
) :
    BaseViewModel<IOnBoardingCardCvv.State>(), IOnBoardingCardCvv.ViewModel {
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
}
