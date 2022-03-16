package com.digitify.testyappakistan.featureflag

interface SPFeatureFlagClient {
    fun hasFeature(flag: String, hasFeatureEnable: (Boolean) -> Unit)
    suspend fun getLocalFeatureFlags()
}