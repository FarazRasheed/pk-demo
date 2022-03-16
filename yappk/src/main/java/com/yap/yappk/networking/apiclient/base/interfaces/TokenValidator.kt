package com.yap.yappk.networking.apiclient.base.interfaces

internal interface TokenValidator {
    var tokenRefreshInProgress: Boolean
    fun invalidate()
}