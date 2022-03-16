package com.yap.yappk.pk.ui.onboarding.topqueue

import androidx.databinding.ObservableField
import com.digitify.core.base.BaseState
import javax.inject.Inject

class ReachedQueueTopState @Inject constructor() : BaseState(), IReachedQueueTop.State {
    override var firstName: ObservableField<String> = ObservableField("")
    override var countryCode: ObservableField<String> = ObservableField("")
    override var mobileNo: ObservableField<String> = ObservableField("")
}