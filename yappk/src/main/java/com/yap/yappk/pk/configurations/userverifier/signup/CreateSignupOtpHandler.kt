package com.yap.yappk.pk.configurations.userverifier.signup

import com.digitify.core.ICreateOtpVerifier
import com.digitify.core.base.usecasebase.UseCaseCallback
import javax.inject.Inject

class CreateSignupOtpHandler @Inject constructor(
    private val createSignupOtpUC: PkCreateSignupOtpUC
) : ICreateOtpVerifier {

    override fun createOtp(
        countryCode: String,
        mobileNo: String,
        completion: (Result<Boolean>) -> Unit
    ) {
        createSignupOtpUC.executeUseCase(
            requestValues = PkCreateSignupOtpUC.RequestValues(countryCode, mobileNo),
            responseCallback = object :
                UseCaseCallback<PkCreateSignupOtpUC.ResponseValue, PkCreateSignupOtpUC.ErrorValue> {

                override fun onSuccess(response: PkCreateSignupOtpUC.ResponseValue) {
                    completion.invoke(Result.success(response.result))
                }

                override fun onError(error: PkCreateSignupOtpUC.ErrorValue) {
                    completion.invoke(Result.failure(Throwable(error.msg)))
                }
            })
    }
}