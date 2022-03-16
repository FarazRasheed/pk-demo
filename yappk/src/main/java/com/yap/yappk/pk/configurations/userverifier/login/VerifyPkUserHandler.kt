package com.yap.yappk.pk.configurations.userverifier.login

import com.digitify.core.IUserVerifier
import com.digitify.core.base.usecasebase.UseCaseCallback
import javax.inject.Inject

class VerifyPkUserHandler @Inject constructor(
    private val verifyPkUserUC: VerifyPkUserUC
) : IUserVerifier {

    override fun verifyUser(
        mobileNo: String,
        completion: (Result<Boolean>) -> Unit
    ) {
        verifyPkUserUC.executeUseCase(
            requestValues = VerifyPkUserUC.RequestValues(mobileNo),
            responseCallback = object :
                UseCaseCallback<VerifyPkUserUC.ResponseValue, VerifyPkUserUC.ErrorValue> {

                override fun onSuccess(response: VerifyPkUserUC.ResponseValue) {
                    completion.invoke(Result.success(response.result))
                }

                override fun onError(error: VerifyPkUserUC.ErrorValue) {
                    completion.invoke(Result.failure(Throwable(error.msg)))
                }
            })
    }
}