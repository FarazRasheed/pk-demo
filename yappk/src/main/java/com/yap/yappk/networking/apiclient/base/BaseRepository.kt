package com.yap.yappk.networking.apiclient.base

import com.yap.yappk.networking.apiclient.base.interfaces.IRepository
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import com.google.gson.stream.MalformedJsonException as MalformedJsonException1

const val MALFORMED_JSON_EXCEPTION_CODE = 0

abstract class BaseRepository : IRepository {
    private val defaultErrorMessage =
        "Sorry, that doesn't look right. We’re working on fixing it now. Please try again in sometime."
    private val defaultConnectionErrorMessage =
        "Looks like you're offline. Please reconnect and refresh to continue."
    private val defaultSessionMessage =
        "Your session has been expired"

    override suspend fun <T : BaseApiResponse> executeSafely(call: suspend () -> Response<T>): ApiResponse<T> {
        try {
            val response: Response<T> = call.invoke()
            if (response.isSuccessful) {
                return ApiResponse.Success(response.code(), response.body()!!)
            }

            // Check if this is not a server side error (4** or 5**) then return error instead of success
            return ApiResponse.Error(detectError(response))

        } catch (exception: MalformedJsonException1) {
            return ApiResponse.Error(
                ApiError(
                    MALFORMED_JSON_EXCEPTION_CODE,
                    exception.localizedMessage ?: ""
                )
            )
        } catch (exception: Exception) {
            return ApiResponse.Error(
                ApiError(
                    getDefaultCode(),
                    exception.localizedMessage ?: ""
                )
            )
        }
    }

    fun <T : BaseApiResponse> detectError(response: Response<T>): ApiError {
        return when (response.code()) {
            401 -> getApiError(mapError(NetworkErrors.SessionExpire, response.code()))
            403 -> getApiError(mapError(NetworkErrors.Forbidden, response.code()))
            404 -> getApiError(mapError(NetworkErrors.NotFound, response.code()))
            502 -> getApiError(mapError(NetworkErrors.BadGateway, response.code()))
            504 -> getApiError(mapError(NetworkErrors.NoInternet, response.code()))
            in 400..500 -> getApiError(
                mapError(
                    NetworkErrors.InternalServerError(response.errorBody()?.string()),
                    response.code()
                )
            )
            -1009 -> getApiError(mapError(NetworkErrors.NoInternet, response.code()))
            -1001 -> getApiError(mapError(NetworkErrors.RequestTimedOut, response.code()))
            else -> {
                getApiError(mapError(NetworkErrors.UnknownError(), response.code()))
            }
        }
    }

    private fun getError(code: Int, response: String?): ServerError {
        response?.let {
            if (it.isNotBlank()) {
                try {
                    val obj = JSONObject(it)

                    if (obj.has("errors")) {
                        val errors = obj.getJSONArray("errors")
                        if (errors.length() > 0) {
                            val message = errors.getJSONObject(0).getString("message")
                            val actualCode = errors.getJSONObject(0).getString("code")
                            return if (message != "null") {
                                ServerError(
                                    code,
                                    errors.getJSONObject(0).getString("message"),
                                    actualCode
                                )
                            } else {
                                ServerError(code, defaultErrorMessage, actualCode)
                            }
                        }
                    } else if (obj.has("error")) {
                        // most probably.. unauthorised error
                        val error = obj.getString("error") ?: defaultErrorMessage
                        if (error.contains("unauthorized")) {
                            return ServerError(0, defaultErrorMessage)
                        }
                        return ServerError(0, error)
                    }
                } catch (e: JSONException) {
                    ServerError(code, defaultErrorMessage)
                }
            }
        }
        return ServerError(getDefaultCode(), defaultErrorMessage)
    }

    private fun getApiError(error: ServerError): ApiError {
        return ApiError(
            error.code ?: getDefaultCode(),
            error.message ?: defaultErrorMessage,
            error.actualCode
        )
    }

    private fun mapError(error: NetworkErrors, code: Int = 0): ServerError {
        return when (error) {

            is NetworkErrors.NoInternet, NetworkErrors.RequestTimedOut -> ServerError(
                code,
                defaultConnectionErrorMessage
            )

            is NetworkErrors.BadGateway -> ServerError(code, defaultErrorMessage)
            is NetworkErrors.NotFound -> ServerError(code, defaultErrorMessage)
            is NetworkErrors.Forbidden -> ServerError(
                code,
                "You don't have access to this information"
            )
            is NetworkErrors.InternalServerError -> getError(code, error.response)
            is NetworkErrors.SessionExpire -> ServerError(code, defaultSessionMessage)
            is NetworkErrors.UnknownError -> ServerError(code, defaultErrorMessage)
        }
    }


    private fun getDefaultCode(): Int {
        return 0
    }

    data class ServerError(val code: Int?, val message: String?, val actualCode: String = "-1")


    protected val MULTIPART = "multipart/form-dataList"

    sealed class NetworkErrors {
        object NoInternet : NetworkErrors()
        object RequestTimedOut : NetworkErrors()
        object BadGateway : NetworkErrors()
        object NotFound : NetworkErrors()
        object Forbidden : NetworkErrors()
        object SessionExpire : NetworkErrors()
        class InternalServerError(val response: String?) : NetworkErrors()
        open class UnknownError : NetworkErrors()
    }
}