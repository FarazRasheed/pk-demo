package com.digitify.testyappakistan.featureflag

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class SPFeatureFlagProvider @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineContext // Dispatcher added for Api call in future
) :
    SPFeatureFlagClient {

    private var featureFlags = mutableListOf<SPFeatureFlag>()

    override suspend fun getLocalFeatureFlags() {
        coroutineScope {
            featureFlags.clear()
            featureFlags.addAll(getFlagsFromLocalResources().features)
        }
    }

    private fun getFlagsFromLocalResources(): SPFeatureFlagsList {
        val gson = GsonBuilder().create()
        val jsonString = context.assets.open(FEATURE_FLAG_FILE_NAME).bufferedReader().use {
            it.readText()
        }
        val itemType = object : TypeToken<SPFeatureFlagsList>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

    override fun hasFeature(flag: String, hasFeatureEnable: (Boolean) -> Unit) {
        if (featureFlags.isNullOrEmpty()) {
            hasFeatureEnable(false)
        } else {
            val isEnabled = featureFlags.first {
                it.key == flag
            }.value
            hasFeatureEnable(isEnabled)
        }
    }
}