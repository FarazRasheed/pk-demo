package com.yap.yappk.networking.apiclient.base.intercepters

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.CookiesManager
import com.yap.yappk.networking.apiclient.base.interfaces.TokenValidator
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.requestdtos.TokenRefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal abstract class SessionValidator : TokenValidator, Interceptor {
    @Inject
    lateinit var authApi: AuthApi

    override var tokenRefreshInProgress: Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        // Check if user is logged in and server revoked the access token.
        if (CookiesManager.isLoggedIn && response.code == 401) {
            // need to refresh the token since previous token was invalid

            if (!tokenRefreshInProgress) {
                // Refresh token
                if (this::authApi.isInitialized) {
                    tokenRefreshInProgress = true
                    when (runBlocking {
                        authApi.refreshJWTToken(
                            TokenRefreshRequest(
                                id_token = CookiesManager.jwtToken,
                                grant_type = "refresh"
                            )
                        )
                    }) {
                        is ApiResponse.Success -> {
                            val builder =
                                request.newBuilder()
                                    .header(KEY_AUTHORIZATION, KEY_BEARER + CookiesManager.jwtToken)
                                    .method(request.method, request.body)
                            response = chain.proceed(builder.build())
                        }
                        else -> {
                            // Error in Refreshing token, Invalidate user now
                            sessionExpire()
                        }
                    }
                } else
                    sessionExpire()
                tokenRefreshInProgress = false
            }
        }
        return response
    }

    private fun sessionExpire() {
        CookiesManager.isLoggedIn = false
        CookiesManager.jwtToken = ""
        invalidate()
    }
}