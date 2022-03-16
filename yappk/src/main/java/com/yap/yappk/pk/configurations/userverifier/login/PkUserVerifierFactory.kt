package com.yap.yappk.pk.configurations.userverifier.login

import com.digitify.core.IUserVerifier
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.CustomersRepository
import com.yap.yappk.networking.microservices.customers.CustomersRetroService
import com.yap.yappk.pk.configurations.AppConfigurations
import kotlinx.coroutines.Dispatchers

class PkUserVerifierFactory {

    fun create(): IUserVerifier {
        val customersService =
            AppConfigurations.getRetrofit()?.createService(CustomersRetroService::class.java)
        val customersApi: CustomersApi = CustomersRepository(customersService!!)
        val verifyPkUserUC = VerifyPkUserUC(customersApi, Dispatchers.IO)

        return VerifyPkUserHandler(verifyPkUserUC)
    }
}