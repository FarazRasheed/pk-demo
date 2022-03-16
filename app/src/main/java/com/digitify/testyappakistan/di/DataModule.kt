package com.digitify.testyappakistan.di

import android.content.Context
import com.digitify.core.analytics.AnalyticsEvent
import com.digitify.testyappakistan.DeeplinkIntentHandler
import com.digitify.testyappakistan.eventtrackers.AnalyticsEventsHandler
import com.digitify.testyappakistan.featureflag.SPFeatureFlagClient
import com.digitify.testyappakistan.featureflag.SPFeatureFlagProvider
import com.digitify.testyappakistan.onboarding.accountSelection.AccountSelectionAnimation
import com.digitify.testyappakistan.onboarding.countryCodePicker.CreateCountryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideAccountAnimation() = AccountSelectionAnimation()

    @Singleton
    @Provides
    fun provideCreateCountryAdapter(featureFlagProvider: SPFeatureFlagClient) =
        CreateCountryAdapter(featureFlagProvider)

    @Singleton
    @Provides
    fun provideFeatureFlags(
        @ApplicationContext context: Context,
        dispatcher: CoroutineContext
    ): SPFeatureFlagClient {
        return SPFeatureFlagProvider(context, dispatcher)
    }

    @Singleton
    @Provides
    fun provideAnalytics(): AnalyticsEvent = AnalyticsEventsHandler()

    @Provides
    fun provideDeeplinkIntentHandler(): DeeplinkIntentHandler {
        return DeeplinkIntentHandler()
    }
}
