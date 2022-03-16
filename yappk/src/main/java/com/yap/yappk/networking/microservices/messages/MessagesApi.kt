package com.yap.yappk.networking.microservices.messages


import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.networking.microservices.messages.requestdtos.VerifyOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.responsedtos.ForgotPasscodeOtpResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.OtpValidationOnBoardingResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.TermsAndConditionsResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.VerifyOtpResponse

interface MessagesApi {

    suspend fun createOtpOnboarding(createOtpOnboardingRequest: CreateOtpOnboardingRequest): ApiResponse<BaseApiResponse>
    suspend fun verifyOtpOnboarding(verifyOtpOnboardingRequest: VerifyOtpOnboardingRequest): ApiResponse<OtpValidationOnBoardingResponse>
    suspend fun getTerms(): ApiResponse<TermsAndConditionsResponse>
    suspend fun getHelpDesk(): ApiResponse<BaseResponse<String>>
    suspend fun createForgotPasscodeOTP(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): ApiResponse<ForgotPasscodeOtpResponse>
    suspend fun verifyForgotPasscodeOTP(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): ApiResponse<ForgotPasscodeOtpResponse>
    suspend fun createOtp(action: String): ApiResponse<BaseApiResponse>
    suspend fun verifyOtp(
        otp: String,
        action: String
    ): ApiResponse<VerifyOtpResponse>

}
