package com.yap.yappk.networking.apiclient.base.interfaces

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import retrofit2.Response

internal interface IRepository {
    suspend fun <T : BaseApiResponse> executeSafely(call: suspend () -> Response<T>): ApiResponse<T>
}