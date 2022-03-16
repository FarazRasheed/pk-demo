package com.yap.yappk.pk.configurations

import com.yap.yappk.networking.apiclient.base.RetroNetwork


object AppConfigurations {

    private var appConfigs: BuildConfigurations? = null
    private var retrofitNetwork: RetroNetwork? = null

    fun init(configs: BuildConfigurations?, retroNetwork: RetroNetwork) {
        appConfigs = configs
        retrofitNetwork = retroNetwork
    }

    fun getAppConfigs(): BuildConfigurations? {
        return appConfigs
    }

    fun getRetrofit(): RetroNetwork? {
        return retrofitNetwork
    }

    fun deInit() {
        appConfigs = null
        retrofitNetwork = null
    }
}
