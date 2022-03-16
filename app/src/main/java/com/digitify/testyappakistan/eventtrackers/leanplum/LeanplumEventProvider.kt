package com.digitify.testyappakistan.eventtrackers.leanplum

import com.digitify.core.analytics.ILeanplumEvent
import com.leanplum.Leanplum

class LeanplumEventProvider : ILeanplumEvent {
    override fun sendLeanplumEventWithoutParams(eventName: String, value: String) {
        if (value.isEmpty()) {
            Leanplum.track(eventName)
        } else {
            Leanplum.track(eventName, value)
        }
    }

    override fun sendLeanplumEventWithParams(eventName: String, params: Map<String, Any>) {
        Leanplum.track(eventName, params)
    }

    override fun sendLeanplumEventUserAttributes(userID: String?, params: Map<String, Any?>) {
        Leanplum.setUserAttributes(userID, params)
    }
}