package com.digitify.testyappakistan.configurations

import com.digitify.core.ICreateOtpVerifier
import com.digitify.core.IUserVerifier
import com.yap.yappk.pk.configurations.userverifier.login.PkUserVerifierFactory
import com.yap.yappk.pk.configurations.userverifier.signup.PkSignupOtpVerifierFactory

class UserVerifierProvider {

    fun provide(countryCode: String?): IUserVerifier {
        return when (countryCode) {
            "+92", "0092" -> {
                val pkUserVerifierFactory = PkUserVerifierFactory()
                pkUserVerifierFactory.create()
            }
            else -> throw IllegalStateException("Country code $countryCode is not supported.")
        }
    }

    fun provideCreateOtpVerifier(countryCode: String?): ICreateOtpVerifier {
        return when (countryCode) {
            "+92", "0092" -> {
                val pkUserVerifierFactory = PkSignupOtpVerifierFactory()
                pkUserVerifierFactory.create()
            }
            else -> throw IllegalStateException("Country code $countryCode is not supported.")
        }
    }
}