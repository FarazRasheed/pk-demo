package com.yap.yappk.pk.configurations.userverifier.signup

import com.digitify.core.base.usecasebase.BaseUseCase
import com.digitify.core.base.usecasebase.UseCaseCallback
import com.digitify.core.enums.AccountType
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PkCreateSignupOtpUC @Inject constructor(
    private val messagesApi: MessagesApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<PkCreateSignupOtpUC.RequestValues, PkCreateSignupOtpUC.ResponseValue, PkCreateSignupOtpUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val otpRequest = CreateOtpOnboardingRequest(
                requestValues?.countryCode,
                requestValues?.mobileNumber,
                AccountType.B2C_ACCOUNT.name
            )
            val response = messagesApi.createOtpOnboarding(otpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue(true))
                    }
                    is ApiResponse.Error -> {
                        responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(val countryCode: String, val mobileNumber: String) :
        BaseUseCase.RequestValues

    class ResponseValue(val result: Boolean) : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}