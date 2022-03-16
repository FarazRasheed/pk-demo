package com.yap.yappk.pk.ui.kyc.main

import androidx.databinding.ObservableInt
import com.digitify.core.base.BaseState
import javax.inject.Inject

class KycDashboardState @Inject constructor() : BaseState(), IKycDashboard.State{
    override var totalProgress: ObservableInt = ObservableInt(100)
    override var currentProgress: ObservableInt = ObservableInt(0)
}