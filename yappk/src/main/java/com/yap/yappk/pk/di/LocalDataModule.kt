package com.yap.yappk.pk.di

import android.content.Context
import com.digitify.core.deeplink.DeeplinkNavigatorPayload
import com.digitify.core.deeplink.IDeeplinkNavigator
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.AppConfigurations
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.deeplinkNavigations.DeeplinkHandler
import com.yap.yappk.pk.deeplinkNavigations.NotificationPayloadImpl
import com.yap.yappk.pk.featureflag.PKFeatureFlagClient
import com.yap.yappk.pk.featureflag.PKFeatureFlagProvider
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.IPasscodeValidator
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.PasscodeValidatorImpl
import com.yap.yappk.pk.ui.kyc.failed.IOnBoardingFailedDataComposer
import com.yap.yappk.pk.ui.kyc.failed.OnBoardingFailedComposer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun providesSessionManager(
        customersApi: CustomersApi,
        authApi: AuthApi,
        sharedPreferenceManager: SharedPreferenceManager
    ) = SessionManager(customersApi, authApi, sharedPreferenceManager)

    @Singleton
    @Provides
    fun providesPkConfigurations(
        @ApplicationContext context: Context
    ): PKBuildConfigurations {
        val pkBuildConfigs = PKBuildConfigurations(context)
        pkBuildConfigs.configManager = AppConfigurations.getAppConfigs()

        return pkBuildConfigs
    }

    @Singleton
    @Provides
    fun provideFeatureFlags(
        @ApplicationContext context: Context,
        dispatcher: CoroutineContext
    ): PKFeatureFlagClient {
        return PKFeatureFlagProvider(context, dispatcher)
    }

    @Provides
    fun providePasscodeValidator(resourcesProviders: ResourcesProviders): IPasscodeValidator {
        return PasscodeValidatorImpl(resourcesProviders)
    }

    @Singleton
    @Provides
    fun provideDeeplinkNavigator(
        @ApplicationContext context: Context,
    ): IDeeplinkNavigator {
        return DeeplinkHandler(context)
    }

    @Singleton
    @Provides
    fun provideNotificationPayloadImpl(
        deeplinkHandler: IDeeplinkNavigator,
    ): DeeplinkNavigatorPayload {
        return NotificationPayloadImpl(deeplinkHandler)
    }

    @Provides
    fun provideOnBoardingFailedComposer(): IOnBoardingFailedDataComposer {
        return OnBoardingFailedComposer()
    }
}