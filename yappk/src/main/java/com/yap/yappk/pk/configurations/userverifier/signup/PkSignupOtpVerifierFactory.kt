package com.yap.yappk.pk.configurations.userverifier.signup

import com.digitify.core.ICreateOtpVerifier
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.MessagesRepository
import com.yap.yappk.networking.microservices.messages.MessagesRetroService
import com.yap.yappk.pk.configurations.AppConfigurations
import kotlinx.coroutines.Dispatchers

class PkSignupOtpVerifierFactory {

    fun create(): ICreateOtpVerifier {
        val customersService =
            AppConfigurations.getRetrofit()?.createService(MessagesRetroService::class.java)
        val messagesApi: MessagesApi = MessagesRepository(customersService!!)
        val signupPkUserUC = PkCreateSignupOtpUC(messagesApi, Dispatchers.IO)

        return CreateSignupOtpHandler(signupPkUserUC)
    }
}