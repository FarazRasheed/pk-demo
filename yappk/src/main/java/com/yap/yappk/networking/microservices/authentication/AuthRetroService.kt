package com.yap.yappk.networking.microservices.authentication

import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.microservices.authentication.requestdtos.LoginRequest
import com.yap.yappk.networking.microservices.authentication.requestdtos.TokenRefreshRequest
import com.yap.yappk.networking.microservices.authentication.responsedtos.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetroService {

    // Refresh JWT Token
    @POST(AuthRepository.URL_GET_JWT_TOKEN)
    suspend fun refreshJWTToken(@Body tokenRefreshRequest: TokenRefreshRequest): Response<LoginResponse>

    // Get CSRF Token
    @GET(AuthRepository.URL_GET_CSRF_TOKEN)
    suspend fun getCSRFToken(): Response<BaseApiResponse>

    @POST(AuthRepository.URL_GET_JWT_TOKEN)
    suspend fun login(@Body loginRequest: LoginRequest?): Response<LoginResponse>

    // Logout
    @POST(AuthRepository.URL_LOGOUT)
    suspend fun logout(@Query("uuid") uuid: String): Response<BaseApiResponse>

}