package com.yap.yappk.pk.ui.onboarding.main

import androidx.databinding.ObservableInt
import com.digitify.core.base.BaseState
import java.util.*

class MainState : BaseState(), IMain.State {
    override var startTime: Date? = null
    override var elapsedOnboardingTime: Long = 1L
    override var totalProgress: ObservableInt = ObservableInt(100)
    override var currentProgress: ObservableInt = ObservableInt(0)
}