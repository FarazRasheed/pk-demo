package com.yap.yappk.pk.featureflag

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.networking.microservices.featureflags.PKFeatureFlag
import com.yap.yappk.networking.microservices.featureflags.PKFeatureFlagsList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import okio.buffer
import okio.source
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class PKFeatureFlagProvider @Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineContext // Dispatcher added for Api call in future
) :
    PKFeatureFlagClient {

    private var featureFlags = mutableListOf<PKFeatureFlag>()

    override suspend fun getLocalFeatureFlags() {
        Log.e("PKFeatureFlagProvider", "====>: Get PK feature Flags")
        coroutineScope {
            featureFlags.clear()
            featureFlags.addAll(getFlagsFromLocalResources().features)
        }
    }

    private fun getFlagsFromLocalResources(): PKFeatureFlagsList {
        val gson = GsonBuilder().create()
        val jsonString = context.assets.open(FEATURE_FLAG_FILE_NAME).bufferedReader().use {
            it.readText()
        }
        val itemType = object : TypeToken<PKFeatureFlagsList>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

    fun readJsonFromAssets(context: Context, filePath: String): String? {
        try {
            val source = context.assets.open(filePath).source().buffer()
            return source.readByteString().string(Charset.forName("utf-8"))

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
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