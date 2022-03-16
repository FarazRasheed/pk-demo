package com.yap.yappk.pk.di

import com.digitify.core.biometric.BiometricUtils
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.pk.ui.auth.AccountRouteManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class UtilityModule {

    @Provides
    fun provideBiometricUtils(): BiometricUtils {
        return BiometricUtils()
    }

    @Provides
    fun provideAccountRouteManager(sharedPreferenceManager: SharedPreferenceManager): AccountRouteManager {
        return AccountRouteManager(sharedPreferenceManager)
    }
}