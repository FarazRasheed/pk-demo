package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.report

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class ReportCardState @Inject constructor() : BaseState(), IReportCard.State {
    override var isCardDamageOption: MutableLiveData<Boolean> = MutableLiveData(null)
    override var helpDeskNumber: MutableLiveData<String>? = MutableLiveData()
}
