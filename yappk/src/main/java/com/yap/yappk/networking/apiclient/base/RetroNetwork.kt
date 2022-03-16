package com.yap.yappk.networking.apiclient.base

import android.content.Context
import android.os.Environment
import com.yap.yappk.BuildConfig
import com.yap.yappk.networking.apiclient.base.intercepters.CookiesInterceptor
import com.yap.yappk.networking.apiclient.base.intercepters.NetworkConstraintsInterceptor
import com.yap.yappk.networking.apiclient.base.intercepters.SessionValidator
import com.yap.yappk.networking.apiclient.base.interfaces.NetworkConstraintsListener
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


private const val timeoutRead = 60L   //In seconds
private const val diskCacheSize = (10 * 1024 * 1024).toLong()
private const val timeoutConnect = 60L   //In seconds

@Singleton
class RetroNetwork @Inject constructor(@ApplicationContext val appContext: Context) {
    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    private lateinit var retrofit: Retrofit
    private var networkConstraintsListener: NetworkConstraintsListener? = null
        get() {
            if (field == null) field = NetworkConstraintsListener.DEFAULT
            return field
        }


    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            return loggingInterceptor
        }

    fun build(baseUrl: String) {
        if (!this::retrofit.isInitialized)
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private val okHttpClient: OkHttpClient
        get() {
            okHttpBuilder.addInterceptor(logger)
            okHttpBuilder.addInterceptor(CookiesInterceptor())
            okHttpBuilder.addInterceptor(object : SessionValidator() {
                override fun invalidate() {
                    networkConstraintsListener?.onSessionInvalid()
                }
            })
            okHttpBuilder.addInterceptor(object : NetworkConstraintsInterceptor(appContext) {
                override fun onInternetUnavailable() {
                    networkConstraintsListener?.onInternetUnavailable()
                }

                override fun onCacheUnavailable() {
                    networkConstraintsListener?.onCacheUnavailable()
                }
            })
            okHttpBuilder.connectTimeout(timeoutConnect, TimeUnit.SECONDS)
            okHttpBuilder.readTimeout(timeoutRead, TimeUnit.SECONDS)
            okHttpBuilder.writeTimeout(timeoutRead, TimeUnit.SECONDS)
            okHttpBuilder.retryOnConnectionFailure(true)
            okHttpBuilder.cache(getDiskCache())
            return okHttpBuilder.build()
        }

    private fun getDiskCache(): Cache {
        val cacheDir = File(Environment.getDataDirectory(), "cache")
        return Cache(cacheDir, diskCacheSize)
    }

    fun listenNetworkConstraints(listener: NetworkConstraintsListener) {
        this.networkConstraintsListener = listener
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

}
