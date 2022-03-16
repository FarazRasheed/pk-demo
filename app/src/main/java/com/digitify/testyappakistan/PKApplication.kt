package com.digitify.testyappakistan

import android.app.Application
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


/**
Created by Faheem Riaz on 04/08/2021.
 **/

@HiltAndroidApp
class PKApplication : Application() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreate() {
        super.onCreate()
        initializePkConfigs()
    }

    private fun initializePkConfigs() {
        initializeAdjustSdk(BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE, sharedPreferenceManager)
    }
}