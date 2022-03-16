package com.yap.yappk.pk.ui.kyc.cardorder.cardpaymentdashboard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardPaymentDashboardState @Inject constructor() : BaseState(), ICardPaymentDashboard.State {
    override val tbTitle: MutableLiveData<String> = MutableLiveData()
}