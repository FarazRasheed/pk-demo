package com.digitify.testyappakistan.eventtrackers

import com.digitify.core.analytics.AnalyticsEvent
import com.digitify.core.analytics.IAdjustEvent
import com.digitify.core.analytics.IFirebaseEvent
import com.digitify.core.analytics.ILeanplumEvent
import com.digitify.testyappakistan.eventtrackers.adjust.AdjustEventProvider
import com.digitify.testyappakistan.eventtrackers.firebase.FirebaseEventProvider
import com.digitify.testyappakistan.eventtrackers.leanplum.LeanplumEventProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsEventsHandler @Inject constructor() : AnalyticsEvent {

    override val firebase: IFirebaseEvent
    override val leanplum: ILeanplumEvent
    override val adjust: IAdjustEvent

    init {
        firebase = FirebaseEventProvider()
        leanplum = LeanplumEventProvider()
        adjust = AdjustEventProvider()
    }
}