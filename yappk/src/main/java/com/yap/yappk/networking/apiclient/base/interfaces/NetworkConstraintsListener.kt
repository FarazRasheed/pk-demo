package com.yap.yappk.networking.apiclient.base.interfaces

interface NetworkConstraintsListener {

    fun onInternetUnavailable()
    fun onCacheUnavailable()
    fun onSessionInvalid()

    companion object {
        val DEFAULT = object : NetworkConstraintsListener {
            override fun onInternetUnavailable() = Unit

            override fun onCacheUnavailable() = Unit

            override fun onSessionInvalid() = Unit
        }
    }
}