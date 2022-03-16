package com.yap.yappk.networking.apiclient.base.intercepters

import android.text.TextUtils
import com.yap.yappk.networking.apiclient.base.CookiesManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

const val KEY_AUTHORIZATION = "Authorization"
const val KEY_BEARER = "Bearer "

internal class CookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addCookiesInRequest(chain.request())
        return chain.proceed(request)
    }

    private fun addCookiesInRequest(request: Request): Request {
        val builder = request.newBuilder()
        if (!TextUtils.isEmpty(CookiesManager.jwtToken)) {
            builder.addHeader(KEY_AUTHORIZATION, KEY_BEARER + CookiesManager.jwtToken)
        }
        return builder.build()
    }
}
