package com.yap.yappk.pk.ui.onboarding.waitinglist

import androidx.databinding.ObservableField
import com.digitify.core.base.BaseState
import javax.inject.Inject

class WaitingListState @Inject constructor() : BaseState(), IWaitingList.State {
    override var jump: ObservableField<String> = ObservableField("0")
    override var rank: ObservableField<String> = ObservableField("0")
    override var signedUpUsers: ObservableField<String> = ObservableField("0")
    override var waitingBehind: ObservableField<String> = ObservableField("0")
    override var gainPoints: ObservableField<String> = ObservableField("0")
    override var rankList: MutableList<Int> = mutableListOf()
    override var totalGainedPoints: ObservableField<String> = ObservableField("0")

}