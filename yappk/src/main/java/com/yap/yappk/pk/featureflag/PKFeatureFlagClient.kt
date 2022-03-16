package com.yap.yappk.pk.featureflag

interface PKFeatureFlagClient {
    fun hasFeature(flag: String, hasFeatureEnable: (Boolean) -> Unit)
    suspend fun getLocalFeatureFlags()
}