package com.yap.yappk.pk.ui.kyc.cardname.cardnameconfirmation

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class CardNameConfirmationState @Inject constructor() : BaseState(), ICardNameConfirmation.State {
    override val cardName: MutableLiveData<String> = MutableLiveData()
}