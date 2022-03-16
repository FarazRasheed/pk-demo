package com.yap.yappk.pk.ui.onboarding.otpverification

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.SingleClickEvent
import com.digitify.core.enums.AccountType
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import com.yap.yappk.networking.microservices.messages.requestdtos.VerifyOtpOnboardingRequest
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OTPVerificationVM @Inject constructor(
    private val messagesApi: MessagesApi,
    private val resourcesProviders: ResourcesProviders
) : BaseViewModel<IOTPVerification.State>(),
    IOTPVerification.ViewModel {
    override var otp: String = ""
    private val _otpEvent: MutableLiveData<Boolean> = MutableLiveData()
    override val otpEvent: LiveData<Boolean> = _otpEvent
    override var optToken: String? = null
    override val resetOTPEvent: MutableLiveData<Boolean> = MutableLiveData()
    override var mobileNumber: String = ""
    override var countryCode: String = ""
    override val viewState: IOTPVerification.State = OTPVerificationState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    private val _openCreatePassCode = MutableLiveData<SingleEvent<Int>>()
    override val openCreatePassCode: LiveData<SingleEvent<Int>> get() = _openCreatePassCode

    override fun onCreate() {
        super.onCreate()
        reverseTimer(TimeUnit.SECONDS.toSeconds(10))
    }

    override fun verifyOtp(otpRequest: VerifyOtpOnboardingRequest) {
        launch {
            showLoading(onBackGround = true)
            val response = messagesApi.verifyOtpOnboarding(otpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        optToken = response.data.data?.token
                        _otpEvent.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message,false)
                        _otpEvent.value = false
                    }
                }
            }
        }
    }

    override fun reCreateOtp(otpCreationRequest: CreateOtpOnboardingRequest) {
        launch {
            showLoading(true)
            val response = messagesApi.createOtpOnboarding(otpCreationRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        reverseTimer(TimeUnit.SECONDS.toSeconds(10))
                        resetOtp()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun resetOtp() {
        resetOTPEvent.value = true
    }

    override fun getVerifyOtpRequest(): VerifyOtpOnboardingRequest {
        return VerifyOtpOnboardingRequest(
            countryCode = countryCode.replace("+", "00"),
            mobileNo = mobileNumber.replace(" ", ""),
            otp = otp
        )
    }

    override fun getCreateOtpOnBoardingRequest(): CreateOtpOnboardingRequest {
       return CreateOtpOnboardingRequest(
            countryCode = countryCode.replace("+", "00"),
            mobileNo = mobileNumber.replace(" ", ""),
            AccountType.B2C_ACCOUNT.name
        )
    }

    override fun openCreatePassCodeScreen() {
        _openCreatePassCode.value =
            SingleEvent(R.id.action_otpVerificationFragment_to_passcodeFragment)
    }

    private fun reverseTimer(totalSeconds: Long) {
        viewState.color.value = resourcesProviders.getColor(R.color.pkDisabled)
        object : CountDownTimer(TimeUnit.SECONDS.toMillis(totalSeconds), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timerStr =
                    timerString(
                        minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    )
                viewState.timer.value = timerStr
            }

            override fun onFinish() {
                viewState.validResend.value = true
                viewState.color.value = resourcesProviders.getColor(R.color.pkBlueWithAHintOfPurple)
                viewState.timer.value = "00:00"
            }
        }.start()
    }

    private fun timerString(minutes: Long, seconds: Long): String {
        return String.format(
            "%02d:%02d",
            minutes,
            seconds
        )
    }
}