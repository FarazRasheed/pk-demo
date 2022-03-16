package com.yap.yappk.networking.microservices.messages

import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.base.BaseRepository
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.messages.requestdtos.*
import com.yap.yappk.networking.microservices.messages.responsedtos.ForgotPasscodeOtpResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.OtpValidationOnBoardingResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.TermsAndConditionsResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.VerifyOtpResponse
import javax.inject.Inject

class MessagesRepository @Inject constructor(val service: MessagesRetroService) :
    BaseRepository(), MessagesApi {
    override suspend fun createOtpOnboarding(createOtpOnboardingRequest: CreateOtpOnboardingRequest): ApiResponse<BaseApiResponse> =
        executeSafely(call = { service.createOtpOnboarding(createOtpOnboardingRequest) })

    // use the following response to enable the white listing feature-> "OtpValidationOnBoardingResponse"
    override suspend fun verifyOtpOnboarding(verifyOtpOnboardingRequest: VerifyOtpOnboardingRequest): ApiResponse<OtpValidationOnBoardingResponse> =
        executeSafely(call = { service.verifyOtpOnboarding(verifyOtpOnboardingRequest) })

    override suspend fun getTerms(): ApiResponse<TermsAndConditionsResponse> =
        executeSafely(call = { service.getTerms() })

    override suspend fun getHelpDesk(): ApiResponse<BaseResponse<String>> =
        executeSafely(call = { service.getHelpDesk() })

    override suspend fun createForgotPasscodeOTP(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): ApiResponse<ForgotPasscodeOtpResponse> =
        executeSafely(call = {
            service.createForgotPasscodeOTP(
                forgotPasscodeOtpRequest
            )
        })

    override suspend fun verifyForgotPasscodeOTP(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): ApiResponse<ForgotPasscodeOtpResponse> =
        executeSafely(call = {
            service.verifyForgotPasscodeOTP(
                forgotPasscodeOtpRequest
            )
        })

    override suspend fun createOtp(action: String): ApiResponse<BaseApiResponse> =
        executeSafely(call = {
            service.createOtp(
                CreateOtpRequest(action = action)
            )
        })

    override suspend fun verifyOtp(
        otp: String,
        action: String
    ): ApiResponse<VerifyOtpResponse> =
        executeSafely(call = {
            service.verifyOtp(
                VerifyOtpRequest(
                    action = action,
                    otp = otp
                )
            )
        })

    companion object {
        const val URL_CREATE_OTP_ONBOARDING = "/messages/api/otp/sign-up/mobile-no"
        const val URL_VERIFY_OTP_ONBOARDING = "/messages/api/otp/sign-up/verify"
        const val URL_TERMS_CONDITIONS = "/messages/api/terms-and-conditions"
        const val URL_HELP_DESK = "/messages/api/help-desk"
        const val URL_FORGOT_PASSCODE = "/messages/api/otp/action/forgot-password"
        const val URL_OTP = "/messages/api/otp"
    }
}
