package com.yap.yappk.pk.ui.kyc.paymentconfirmation

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardOrderPaymentConfirmationState @Inject constructor() : BaseState(), ICardOrderPaymentConfirmation.State {
    override var orderNewCardFee: MutableLiveData<String> = MutableLiveData("PKR 0.00")
    override var cardAddressTitle: MutableLiveData<String>? = MutableLiveData()
    override var cardAddressSubTitle: MutableLiveData<String>? = MutableLiveData()
}