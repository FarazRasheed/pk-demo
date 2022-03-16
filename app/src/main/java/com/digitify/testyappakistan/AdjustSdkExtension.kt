package com.digitify.testyappakistan

import android.app.Activity
import android.app.Application
import android.net.UrlQuerySanitizer
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.digitify.core.adjustRefferal.ReferralInfo
import com.digitify.core.enums.ProductFlavour
import com.digitify.core.utils.REFERRAL_ID
import com.digitify.core.utils.REFERRAL_TIME
import com.digitify.core.utils.SharedPreferenceManager


/*
* Following sdk's included
* -> Adjust SDK
* */

fun Application.initializeAdjustSdk(
    flavour: String, buildType: String,
    sharedPreferenceManager: SharedPreferenceManager
) {
    val config = AdjustConfig(
        this,
        adjustToken(flavour),
        if (buildType == "release") AdjustConfig.ENVIRONMENT_PRODUCTION else AdjustConfig.ENVIRONMENT_SANDBOX
    )


    when (flavour) {
        ProductFlavour.PROD.flavour -> {
            Adjust.setEnabled(true)
            config.setAppSecret(1, 325677892, 77945854, 746350982, 870707894)

            config.setDefaultTracker("fj4r46p")
            config.setEventBufferingEnabled(true)
            config.setPreinstallTrackingEnabled(true)

        }
        ProductFlavour.PREPROD.flavour -> {
            config.setAppSecret(1, 82588340, 60633897, 806753301, 962146915)
        }
        ProductFlavour.STG.flavour -> {
            config.setAppSecret(1, 1236756048, 110233912, 2039250280, 199413548)
        }
        ProductFlavour.INTERNAL.flavour -> {
            config.setAppSecret(1, 1236756048, 110233912, 2039250280, 199413548)
        }
        ProductFlavour.QA.flavour -> {
            // Yap Pakistan QA
            config.setAppSecret(1, 1431970953, 1216475993, 603051036, 1980410964)
        }
        ProductFlavour.DEV.flavour -> {
            // Yap Pakistan QA
            config.setAppSecret(1, 1431970953, 1216475993, 603051036, 1980410964)
        }
        else -> throw IllegalStateException("Invalid build flavour found $flavour")
    }



    if (buildType != "release") config.setLogLevel(LogLevel.VERBOSE)
    config.setSendInBackground(true)
    config.setOnEventTrackingSucceededListener {}
    config.setOnEventTrackingFailedListener { }
    config.setOnSessionTrackingSucceededListener { }
    config.setOnSessionTrackingFailedListener { }
    config.setOnDeeplinkResponseListener { deepLink ->
        deepLink?.let { uri ->
            val customerId = UrlQuerySanitizer(uri.toString()).getValue(REFERRAL_ID)
            val time = UrlQuerySanitizer(uri.toString()).getValue(REFERRAL_TIME)
            sharedPreferenceManager.setReferralInfo(ReferralInfo(customerId, time))
        }
        true
    }

    config.setOnAttributionChangedListener { attribution -> }

    Adjust.onCreate(config)
    registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
    config.setOnAttributionChangedListener { }

}

private fun adjustToken(flavour: String): String {
    val productFlavour = when (flavour) {
        ProductFlavour.DEV.flavour -> {
            ProductFlavour.DEV
        }
        ProductFlavour.QA.flavour -> {
            ProductFlavour.QA
        }
        ProductFlavour.STG.flavour -> {
            ProductFlavour.STG
        }
        ProductFlavour.PREPROD.flavour -> {
            ProductFlavour.PREPROD
        }
        ProductFlavour.PROD.flavour -> {
            ProductFlavour.PROD
        }
        else -> ProductFlavour.INTERNAL
    }
    return when (productFlavour) {
        ProductFlavour.PROD -> "xty7lf6skgsg"
        ProductFlavour.PREPROD -> "uv1oiis7wni8"
        ProductFlavour.STG -> "am0wjeshw5xc"
        ProductFlavour.QA -> "pa4xup5ybrwg"    // Yap Pakistan QA token
        ProductFlavour.DEV -> "pa4xup5ybrwg"      // Yap Pakistan QA token
        ProductFlavour.INTERNAL -> "am0wjeshw5xc"
    }
}

private class AdjustLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }
}
