package com.yap.yappk.pk.ui.generic.verifyotp

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.R
import com.yap.yappk.localization.common_text_otp_title
import com.yap.yappk.localization.screen_verify_otp_forgot_card_pin_display_text_title
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CreateOtpUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.ui.generic.enums.OTPAction
import com.yap.yappk.pk.ui.generic.usecases.VerifyOtpUC
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class VerifyOtpVM @Inject constructor(
    override val viewState: VerifyOtpState,
    private val resourcesProvider: ResourcesProviders,
    override val sessionManager: SessionManager,
    private val createOtpUC: CreateOtpUC,
    private val verifyOtpUC: VerifyOtpUC
) : BaseViewModel<IVerifyOtp.State>(), IVerifyOtp.ViewModel {
    override var action: String = ""
    override var otp: String = ""
    override var token: String? = null

    private val _isForgotCardPinOtpCreated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    override val isForgotCardPinOtpCreated: LiveData<Boolean> = _isForgotCardPinOtpCreated

    private val _isForgotCardPinOtpVerified: MutableLiveData<Boolean> = MutableLiveData()
    override val isForgotCardPinOtpVerified: LiveData<Boolean> = _isForgotCardPinOtpVerified

    override fun onCreate() {
        super.onCreate()
        reverseTimer(10)
    }

    override fun createOtp(action: String) {
        showLoading()
        createOtpUC.executeUseCase(
            CreateOtpUC.RequestValues(action),
            object :
                UseCaseCallback<CreateOtpUC.ResponseValue, CreateOtpUC.ErrorValue> {
                override fun onSuccess(response: CreateOtpUC.ResponseValue) {
                    hideLoading()
                    _isForgotCardPinOtpCreated.value = true
                }

                override fun onError(error: CreateOtpUC.ErrorValue) {
                    hideLoading()
                    _isForgotCardPinOtpCreated.value = false
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun verifyOtp(otp: String, action: String) {
        showLoading()
        verifyOtpUC.executeUseCase(
            VerifyOtpUC.RequestValues(otp = otp, action = action),
            object :
                UseCaseCallback<VerifyOtpUC.ResponseValue, VerifyOtpUC.ErrorValue> {
                override fun onSuccess(response: VerifyOtpUC.ResponseValue) {
                    hideLoading()
                    token = response.token?.split("%")?.first()
                    _isForgotCardPinOtpVerified.value = true
                }

                override fun onError(error: VerifyOtpUC.ErrorValue) {
                    hideLoading()
                    _isForgotCardPinOtpVerified.value = false
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun getTitle(action: String): String {
        return when (action) {
            OTPAction.FORGOT_CARD_PIN.name -> {
                resourcesProvider.getString(screen_verify_otp_forgot_card_pin_display_text_title)
            }
            OTPAction.IBFT_BENEFICIARY.name -> {
                resourcesProvider.getString(common_text_otp_title)
            }
            else -> ""
        }
    }

    override fun getTbTitle(action: String): String? {
        return when (action) {
            OTPAction.FORGOT_CARD_PIN.name -> {
                ""
            }
            else -> ""
        }
    }

    override fun reverseTimer(totalSeconds: Long) {
        viewState.color.value = resourcesProvider.getColor(R.color.pkDisabled)
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
                viewState.color.value = resourcesProvider.getColor(R.color.pkBlueWithAHintOfPurple)
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