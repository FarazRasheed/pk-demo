package com.yap.yappk.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sadapay.base.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
abstract class BaseTestCase {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    protected val mockWebServer = MockWebServer()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    inline fun <reified T : Any> getApiMockResponse(
        fileName: String,
        noinline completionHandler: ((data: T) -> Unit)? = null
    ) {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<T>() {}.type
        completionHandler?.let {
            it.invoke(gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType))
        }
    }
}