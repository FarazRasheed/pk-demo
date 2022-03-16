package com.yap.yappk.pk.ui.auth.otpverification

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.KEY_IS_USER_LOGGED_IN
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.featureflag.PKFeatureFlagClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginOTPVerificationVM @Inject constructor(
    private val authApi: AuthApi,
    private val customersApi: CustomersApi,
    override val sharedPreferenceManager: SharedPreferenceManager,
    override val sessionManager: SessionManager,
    private val resourcesProviders: ResourcesProviders,
    private var pkFeatureFlags: PKFeatureFlagClient
) : BaseViewModel<ILoginOTPVerification.State>(),
    ILoginOTPVerification.ViewModel {
    override val viewState: ILoginOTPVerification.State = LoginOTPVerificationState()

    override var otp: String = ""
    private val _otpVerifiedEvent: MutableLiveData<Boolean> = MutableLiveData()
    override val otpVerifiedEvent: LiveData<Boolean> = _otpVerifiedEvent
    override var optToken: String? = null
    override val resetOTPEvent: MutableLiveData<Boolean> = MutableLiveData()
    override var mobileNumber: String = ""
    override var countryCode: String = ""

    private val _openPermissionSettingsPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openPermissionSettings: LiveData<SingleEvent<Int>> get() = _openPermissionSettingsPrivate

    private val _openWaitingListPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openWaitingList: LiveData<SingleEvent<Int>> get() = _openWaitingListPrivate

    private val _openAllowedUserPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openAllowedUser: LiveData<SingleEvent<Int>> get() = _openAllowedUserPrivate

    private val _openDashboardPrivate = MutableLiveData<SingleEvent<AccountInfo?>>()
    override val openDashboard: LiveData<SingleEvent<AccountInfo?>> get() = _openDashboardPrivate

    override fun onCreate() {
        super.onCreate()
        reverseTimer(10)
    }

    override fun verifyOtp(
        otpRequest: DemographicDataRequest,
        demographicRequest: DemographicDataRequest
    ) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.verifyOTPForDeviceVerification(otpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        response.data.data?.let {
                            setTokens(it)
                            setUserPrefs(otpRequest.clientId ?: "", otpRequest.clientSecret ?: "")
                            demographicRequest.token = optToken
                            postDemographicData(demographicRequest)
                        } ?: hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _otpVerifiedEvent.value = false
                    }
                }
            }
        }
    }

    private fun setTokens(token: String) {
        val tokens = token.split("%")
        optToken = tokens.first()
        if (tokens.size > 1)
            authApi.setJwtToken(tokens.last())
    }

    private fun setUserPrefs(username: String, passcode: String) {
        sharedPreferenceManager.save(KEY_IS_USER_LOGGED_IN, true)
        sharedPreferenceManager.saveUserNameWithEncryption(username)
        sharedPreferenceManager.savePassCodeWithEncryption(passcode)
    }

    private fun postDemographicData(demographicDataRequest: DemographicDataRequest) {
        launch {
            val response = customersApi.postDemographicData(demographicDataRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        _otpVerifiedEvent.value = false
                        hideLoading()
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _otpVerifiedEvent.value = true
                    // Fetch Feature flags
                    getFeatureFlags()
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage, false)
                    _otpVerifiedEvent.value = false
                }
            }
        }
    }

    private fun getFeatureFlags(){
        launch {
            // right now there no Api involved to fetch feature flags
            pkFeatureFlags.getLocalFeatureFlags()
        }
    }

    override fun resendOtp(otpCreationRequest: DemographicDataRequest) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.generateOTPForDeviceVerification(otpCreationRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        reverseTimer(10)
                        resetOtp()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(message = response.error.message)
                    }
                }
            }
        }
    }

    override fun resetOtp() {
        resetOTPEvent.value = true
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


    override fun openBiometricSettingScreen() {
        _openPermissionSettingsPrivate.value =
            SingleEvent(R.id.action_loginOTPVerificationFragment_to_biometricPermissionFragment)
    }

    override fun openWaitingListScreen() {
        _openWaitingListPrivate.value = SingleEvent(R.id.waitingListFragment)
    }

    override fun openAllowedUserScreen() {
        _openAllowedUserPrivate.value =
            SingleEvent(R.id.action_loginOTPVerificationFragment_to_reachedQueueTopFragment)
    }

    override fun openDashboardScreen() {
        _openDashboardPrivate.value = SingleEvent(sessionManager.userAccount.value)
    }

}