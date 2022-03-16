package com.yap.yappk.networking.microservices.messages

import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.messages.requestdtos.*
import com.yap.yappk.networking.microservices.messages.responsedtos.ForgotPasscodeOtpResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.OtpValidationOnBoardingResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.TermsAndConditionsResponse
import com.yap.yappk.networking.microservices.messages.responsedtos.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MessagesRetroService {
    // Create otp for mobile number
    @POST(MessagesRepository.URL_CREATE_OTP_ONBOARDING)
    suspend fun createOtpOnboarding(@Body createOtpOnboardingRequest: CreateOtpOnboardingRequest): Response<BaseApiResponse>

    // Verify otp for mobile number
    @PUT(MessagesRepository.URL_VERIFY_OTP_ONBOARDING)
    suspend fun verifyOtpOnboarding(@Body verifyOtpOnboardingRequest: VerifyOtpOnboardingRequest): Response<OtpValidationOnBoardingResponse>

    @GET(MessagesRepository.URL_TERMS_CONDITIONS)
    suspend fun getTerms(): Response<TermsAndConditionsResponse>

    @GET(MessagesRepository.URL_HELP_DESK)
    suspend fun getHelpDesk(): Response<BaseResponse<String>>

    //forgot passcode create otp
    @POST(MessagesRepository.URL_FORGOT_PASSCODE)
    suspend fun createForgotPasscodeOTP(@Body forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): Response<ForgotPasscodeOtpResponse>

    //forgot passcode verify otp
    @PUT(MessagesRepository.URL_FORGOT_PASSCODE)
    suspend fun verifyForgotPasscodeOTP(@Body forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest): Response<ForgotPasscodeOtpResponse>

    //forgot card create otp
    @POST(MessagesRepository.URL_OTP)
    suspend fun createOtp(@Body createOtpForgotCardPinRequest: CreateOtpRequest): Response<BaseApiResponse>

    //forgot card pin verify otp
    @PUT(MessagesRepository.URL_OTP)
    suspend fun verifyOtp(@Body verifyOtpForgotCardPinRequest: VerifyOtpRequest): Response<VerifyOtpResponse>

}