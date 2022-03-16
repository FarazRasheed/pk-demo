package com.yap.yappk.pk.ui.kyc.cardname.updatecardname

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class UpdateCardNameState @Inject constructor() : BaseState(), IUpdateCardName.State {
    override val cardName: MutableLiveData<String> = MutableLiveData()
    override val count: MutableLiveData<String> = MutableLiveData()
    override val isValid: MutableLiveData<Boolean> = MutableLiveData(false)
}