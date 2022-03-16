package com.yap.yappk.networking.microservices.authentication

import com.yap.yappk.networking.apiclient.base.*
import com.yap.yappk.networking.microservices.authentication.requestdtos.LoginRequest
import com.yap.yappk.networking.microservices.authentication.requestdtos.TokenRefreshRequest
import com.yap.yappk.networking.microservices.authentication.responsedtos.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val service: AuthRetroService) : BaseRepository(),
    AuthApi {

    override suspend fun refreshJWTToken(tokenRefreshRequest: TokenRefreshRequest): ApiResponse<LoginResponse> {
        val response = executeSafely(call = { service.refreshJWTToken(tokenRefreshRequest) })
        when (response) {
            is ApiResponse.Success -> {
                CookiesManager.jwtToken = response.data.accessToken
                CookiesManager.isLoggedIn = true
            }
            is ApiResponse.Error -> {

            }
        }
        return response

    }

    override suspend fun getCSRFToken(): ApiResponse<BaseApiResponse> {
        val response: ApiResponse<BaseApiResponse> =
            executeSafely(call = { service.getCSRFToken() })
        when (response) {
            is ApiResponse.Error -> {
                if (response.error.statusCode == MALFORMED_JSON_EXCEPTION_CODE) {
                    // this is expected response so mark it a success
                    return ApiResponse.Success(200, BaseApiResponse())
                }
            }
            is ApiResponse.Success -> {

            }
        }
        return response
    }

    override suspend fun login(loginRequest: LoginRequest?): ApiResponse<LoginResponse> {
        val response =
            executeSafely(call = { service.login(loginRequest) })

        when (response) {
            is ApiResponse.Success -> {
                CookiesManager.jwtToken = response.data.accessToken
                CookiesManager.isLoggedIn = true
            }
            is ApiResponse.Error -> {

            }
        }
        return response
    }

    override suspend fun logout(uuid: String): ApiResponse<BaseApiResponse> {
        val response = executeSafely(call = { service.logout(uuid) })
        when (response) {
            is ApiResponse.Success -> {
                CookiesManager.jwtToken = ""
                CookiesManager.isLoggedIn = false
            }
            is ApiResponse.Error -> {

            }
        }
        return response
    }

    override fun getJwtToken(): String? {
        return CookiesManager.jwtToken
    }

    override fun setJwtToken(token: String?) {
        CookiesManager.jwtToken = token
    }

    companion object {
        const val URL_GET_CSRF_TOKEN = "/auth/login"
        const val URL_GET_JWT_TOKEN = "/auth/oauth/oidc/login-token"
        const val URL_LOGOUT = "/auth/oauth/oidc/logout"

    }
}
