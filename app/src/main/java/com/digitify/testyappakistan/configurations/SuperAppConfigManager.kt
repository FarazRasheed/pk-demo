package com.digitify.testyappakistan.configurations

import android.content.Context
import android.content.Intent
import com.digitify.core.analytics.AnalyticsEvent
import com.digitify.core.configurations.BuildConfigs
import com.digitify.core.enums.ProductFlavour
import com.digitify.core.extensions.newIntent
import com.digitify.core.utils.NAVIGATION_GRAPH_ID
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.digitify.testyappakistan.BuildConfig
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.onboarding.demo.DemoMainActivity
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.utils.enums.PkAppEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuperAppConfigManager @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val analyticsEvent: AnalyticsEvent
) {

    fun initYapRegion(countryDialCode: String?) {
        when (countryDialCode) {
            "+92", "0092" -> initYapPakistan(context = appContext)
            else -> throw IllegalStateException("Configuration has not been handled for $countryDialCode")
        }
    }

    private fun initYapPakistan(context: Context): PKBuildConfigurations {
        val pkConfigs = PKBuildConfigurations(context)
        pkConfigs.configure(
            config = basicConfigs(),
            googleMapsApiKey = context.getString(R.string.google_maps_key),
            analyticsEvent = analyticsEvent
        ) { event ->
            runAppEvent(event, context)
        }
        pkConfigs.setAdjustAppId(appId = getAdjustReferralTrackerId())
        return pkConfigs
    }

    private fun runAppEvent(event: PkAppEvent, context: Context) {
        when (event) {
            PkAppEvent.LOGOUT -> {
                startDemoActivity(context)
            }
            PkAppEvent.IS_LOGGED_IN -> {
            }
        }
    }


    private fun startDemoActivity(context: Context) {
        val intent = newIntent<DemoMainActivity>(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(NAVIGATION_GRAPH_ID, R.navigation.nav_graph)
        intent.putExtra(
            NAVIGATION_GRAPH_START_DESTINATION_ID,
            R.id.loginFragment
        )
        context.startActivity(intent)
    }


    private fun getAdjustReferralTrackerId(): String {
        return when (BuildConfig.FLAVOR) {
            ProductFlavour.DEV.flavour, ProductFlavour.QA.flavour -> "fj4r46p"
            else -> throw IllegalStateException("There is no app has been created on adjust dashboard against this flavour:=> ${BuildConfig.FLAVOR}")
        }
    }

    private fun basicConfigs(): BuildConfigs =
        BuildConfigs(
            flavour = BuildConfig.FLAVOR,
            buildType = BuildConfig.BUILD_TYPE,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toString(),
            BuildConfig.APPLICATION_ID
        )
}