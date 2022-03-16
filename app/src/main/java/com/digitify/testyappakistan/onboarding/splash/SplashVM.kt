/*
 * *
 *  * Created by faheem riaz on 25/08/2021, 11:18 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 11:18 AM
 *
 */

package com.digitify.testyappakistan.onboarding.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.KEY_IS_FIRST_TIME_USER
import com.digitify.core.utils.KEY_IS_USER_LOGGED_IN
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.digitify.testyappakistan.configurations.SuperAppConfigManager
import com.digitify.testyappakistan.featureflag.SPFeatureFlagClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val sharedPreferenceManager: SharedPreferenceManager,
    private var featureFlags: SPFeatureFlagClient,
    private val loadConfig: SuperAppConfigManager
) : BaseViewModel<ISplash.State>(), ISplash.ViewModel {
    override val viewState: ISplash.State = SplashState()

    private val _openAuthentication = MutableLiveData<SingleEvent<Boolean?>>()
    override val openAuthentication: LiveData<SingleEvent<Boolean?>> get() = _openAuthentication

    private val _openDemo = MutableLiveData<SingleEvent<Boolean?>>()
    override val openDemo: LiveData<SingleEvent<Boolean?>> get() = _openDemo

    private val _openDemoWithLogin = MutableLiveData<SingleEvent<Boolean?>>()
    override val openDemoWithLogin: LiveData<SingleEvent<Boolean?>> get() = _openDemoWithLogin

    override fun onCreate() {
        super.onCreate()
        getFeatureFlags()
    }


    private fun getFeatureFlags() {
        // right now there no Api involved to fetch feature flags
        launch {
            featureFlags.getLocalFeatureFlags()
        }
    }

    override fun moveNext() {
        when {
            sharedPreferenceManager.getValueBoolien(KEY_IS_FIRST_TIME_USER, true) -> {
                sharedPreferenceManager.save(KEY_IS_FIRST_TIME_USER, false)
                openDemoScreen()
            }
            sharedPreferenceManager.getValueBoolien(KEY_IS_USER_LOGGED_IN, false) -> {
                setRegionBasedConfigs(
                    sharedPreferenceManager.getDecryptedUserDialCode().toString().replace("+", "00")
                )
                openAuthenticationScreen()
            }
            else -> {
                openDemoWithLoginScreen()
            }
        }
    }

    override fun openAuthenticationScreen() {
        _openAuthentication.value = SingleEvent(true)
    }

    override fun openDemoScreen() {
        _openDemo.value = SingleEvent(true)
    }

    override fun openDemoWithLoginScreen() {
        _openDemoWithLogin.value = SingleEvent(true)
    }

    private fun setRegionBasedConfigs(code: String) {
        viewState.selectedRegion.value = "PK"
        loadConfig.initYapRegion(code)
    }
}