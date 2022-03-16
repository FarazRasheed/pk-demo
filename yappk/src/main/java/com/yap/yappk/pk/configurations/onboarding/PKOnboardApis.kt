package com.yap.yappk.pk.configurations.onboarding

import com.digitify.core.enums.AccountType
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.RetroNetwork
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersRepository
import com.yap.yappk.networking.microservices.customers.CustomersRetroService
import com.yap.yappk.networking.microservices.messages.MessagesRepository
import com.yap.yappk.networking.microservices.messages.MessagesRetroService
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import javax.inject.Inject


class PKOnboardApis @Inject constructor(private val retroNetwork: RetroNetwork) {
    fun doCreateOtp(
        phoneNumber: String?,
        countryCode: String?,
        callback: (Any) -> Unit
    ) {
        val createOtp = CreateOtpOnboardingRequest(
            countryCode,
            phoneNumber,
            AccountType.B2C_ACCOUNT.name
        )
        val viewModel = OTPViewModel()
        viewModel.messagesApi =
            MessagesRepository(retroNetwork.createService(MessagesRetroService::class.java))
        viewModel.createOtp(createOtp) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    callback(true)
                }
                is ApiResponse.Error -> {
                    callback(response.error.message)
                }
            }
        }
    }


    fun verifyUser(
        user: String,
        callback: (Any) -> Unit
    ) {
        val viewModel = LoginViewModel()
        viewModel.customersApi =
            CustomersRepository(retroNetwork.createService(CustomersRetroService::class.java))

        viewModel.verifyUser(user) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val status = (response.data as BaseResponse<*>).data ?: false
                    callback(status)
                }
                is ApiResponse.Error -> {
                    callback(response.error.actualCode)
                }
            }
        }
    }
}