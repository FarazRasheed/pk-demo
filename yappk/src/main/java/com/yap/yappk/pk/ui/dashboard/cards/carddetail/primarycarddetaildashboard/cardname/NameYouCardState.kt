package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardname

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class NameYouCardState @Inject constructor() : BaseState(), INameYouCard.State {
    override var valid: MutableLiveData<Boolean> = MutableLiveData()
}