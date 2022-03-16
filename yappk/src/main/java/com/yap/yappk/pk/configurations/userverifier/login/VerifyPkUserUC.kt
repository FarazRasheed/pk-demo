package com.yap.yappk.pk.configurations.userverifier.login

import com.digitify.core.base.usecasebase.BaseUseCase
import com.digitify.core.base.usecasebase.UseCaseCallback
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VerifyPkUserUC @Inject constructor(
    private val customersApi: CustomersApi,
    private val ioDispatcher: CoroutineContext
) : BaseUseCase<VerifyPkUserUC.RequestValues, VerifyPkUserUC.ResponseValue, VerifyPkUserUC.ErrorValue>() {

    override fun executeUseCase(
        requestValues: RequestValues?,
        responseCallback: UseCaseCallback<ResponseValue, ErrorValue>?
    ) {
        CoroutineScope(ioDispatcher).launch {
            val response =
                customersApi.verifyUser(requestValues?.mobileNumber ?: "")
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        responseCallback?.onSuccess(ResponseValue(response.data.data ?: false))
                    }
                    is ApiResponse.Error -> {
                        if (response.error.actualCode == "AD-10018")
                            responseCallback?.onError(ErrorValue(msg = response.error.actualCode))
                        else
                            responseCallback?.onError(ErrorValue(msg = response.error.message))
                    }
                }
            }
        }
    }

    class RequestValues(val mobileNumber: String) : BaseUseCase.RequestValues
    class ResponseValue(val result: Boolean) : BaseUseCase.ResponseValue
    class ErrorValue(val msg: String) : ResponseError
}