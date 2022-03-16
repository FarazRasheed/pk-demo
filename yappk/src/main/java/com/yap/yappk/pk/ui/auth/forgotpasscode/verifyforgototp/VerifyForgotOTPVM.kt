package com.yap.yappk.pk.ui.auth.forgotpasscode.verifyforgototp

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class VerifyForgotOTPVM @Inject constructor(
    override val viewState: VerifyForgotOTPState,
    private val messagesApi: MessagesApi,
    private val resourcesProviders: ResourcesProviders
) :
    BaseViewModel<IVerifyForgotOTP.State>(), IVerifyForgotOTP.ViewModel {
    override var mobileNumber: String = ""
    override var otp: String = ""
    override var optToken: String? = null
    
    private val _isForgotOtpCreated: MutableLiveData<Boolean> = MutableLiveData()
    override val isForgotOtpCreated: LiveData<Boolean> = _isForgotOtpCreated

    private val _isForgotOtpVerified: MutableLiveData<Boolean> = MutableLiveData()
    override val isForgotOtpVerified: LiveData<Boolean> = _isForgotOtpVerified

    private val _openCreateNewPasscode = MutableLiveData<SingleEvent<Int>>()
    override val openCreateNewPasscode: LiveData<SingleEvent<Int>> get() = _openCreateNewPasscode

    override fun onCreate() {
        super.onCreate()
        reverseTimer(10)
    }

    override fun resendOtp(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest) {
        launch {
            showLoading(true)
            val response = messagesApi.createForgotPasscodeOTP(forgotPasscodeOtpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isForgotOtpCreated.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isForgotOtpCreated.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun verifyOtp(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest) {
        launch {
            showLoading(true)
            val response = messagesApi.verifyForgotPasscodeOTP(forgotPasscodeOtpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        optToken = response.data.data
                        _isForgotOtpVerified.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isForgotOtpVerified.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun reverseTimer(totalSeconds: Long) {
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

    override fun openCreateNewPasscodeScreen() {
        _openCreateNewPasscode.value =
            SingleEvent(R.id.action_verifyForgotOTPFragment_to_createNewPasscodeFragment)
    }

}