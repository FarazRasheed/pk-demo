package com.yap.yappk.networking.apiclient.base.intercepters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.yap.yappk.networking.apiclient.base.interfaces.NetworkConstraintsListener
import okhttp3.Interceptor
import okhttp3.Response


internal abstract class NetworkConstraintsInterceptor(val context: Context) : Interceptor,
    NetworkConstraintsListener {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isInternetAvailable()) {
            onInternetUnavailable()
            request = request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 // 24 hours old only
            ).build()
            val response = chain.proceed(request)
            if (response.cacheResponse == null) {
                onCacheUnavailable()
            }
            return response
        }
        return chain.proceed(request)
    }

    final override fun onSessionInvalid() {
        // Do nothing here because we have separate Interceptor for this
    }


    private fun isInternetAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}