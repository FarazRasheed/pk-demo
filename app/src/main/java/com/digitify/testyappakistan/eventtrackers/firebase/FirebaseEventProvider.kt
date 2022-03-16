package com.digitify.testyappakistan.eventtrackers.firebase

import android.os.Bundle
import androidx.core.os.bundleOf
import com.digitify.core.analytics.IFirebaseEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class FirebaseEventProvider : IFirebaseEvent {
    override fun sendFirebaseEventWithParams(
        eventName: String?,
        screenName: String?,
        additionalParams: Bundle?
    ) {
        val firebaseAnalytics = Firebase.analytics
        eventName?.let { e ->
            val Params =
                bundleOf(FirebaseAnalytics.Param.SCREEN_NAME to (screenName?.trim() ?: ""))
            additionalParams?.let {
                if (it.keySet().size > 0)
                    Params.putAll(it)
            }
            firebaseAnalytics.logEvent(e.trim(), Params)
        }
    }

    override fun sendFirebaseEventWithoutParams(eventName: String?, screenName: String?) {
        val firebaseAnalytics = Firebase.analytics
        eventName?.let { e ->
            firebaseAnalytics.logEvent(e.trim()) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName?.trim() ?: "")
            }
        }
    }

    override fun sendFirebaseEventWithScreenName(screenName: String?) {
        screenName?.let {
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, it)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, javaClass.simpleName)
            }
        }
    }
}