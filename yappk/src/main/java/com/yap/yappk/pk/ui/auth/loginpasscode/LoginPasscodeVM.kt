package com.yap.yappk.pk.ui.auth.loginpasscode

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.KEY_TOUCH_ID_ENABLED
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.localization.screen_verify_passcode_text_account_locked
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.authentication.requestdtos.LoginRequest
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.featureflag.PKFeatureFlagClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LoginPasscodeVM @Inject constructor(
    override val viewState: LoginPasscodeState,
    override val sharedPreferenceManager: SharedPreferenceManager,
    override val sessionManager: SessionManager,
    private val authApi: AuthApi,
    private val customersApi: CustomersApi,
    private val resourcesProviders: ResourcesProviders,
    private val messagesApi: MessagesApi,
    private var pkFeatureFlags: PKFeatureFlagClient
) :
    BaseViewModel<ILoginPassCode.State>(), ILoginPassCode.ViewModel {
    private val _isDeviceValidate: MutableLiveData<Boolean> = MutableLiveData()
    override val isDeviceValidate: LiveData<Boolean> = _isDeviceValidate

    private val _isForgotOtpCreated: MutableLiveData<Boolean> = MutableLiveData()
    override val isForgotOtpCreated: LiveData<Boolean> = _isForgotOtpCreated

    private val _openPermissionSettingsPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openPermissionSettings: LiveData<SingleEvent<Int>> get() = _openPermissionSettingsPrivate

    private val _openWaitingListPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openWaitingList: LiveData<SingleEvent<Int>> get() = _openWaitingListPrivate

    private val _openAllowedUserPrivate = MutableLiveData<SingleEvent<Int>>()
    override val openAllowedUser: LiveData<SingleEvent<Int>> get() = _openAllowedUserPrivate

    private val _openDashboardPrivate = MutableLiveData<SingleEvent<AccountInfo?>>()
    override val openDashboard: LiveData<SingleEvent<AccountInfo?>> get() = _openDashboardPrivate

    private val _openOTPVerification = MutableLiveData<SingleEvent<Int>>()
    override val openOTPVerification: LiveData<SingleEvent<Int>> get() = _openOTPVerification

    private val _openForgotOTPVerification = MutableLiveData<SingleEvent<Int>>()
    override val openForgotOTPVerification: LiveData<SingleEvent<Int>> get() = _openForgotOTPVerification

    override fun getClientSecret(): String {
        return sharedPreferenceManager.getDecryptedPassCode() ?: ""
    }

    override fun isBiometricLoginEnabled(isBiometricAvailable: Boolean): Boolean {
        return if (isBiometricAvailable) {
            sharedPreferenceManager.getValueBoolien(KEY_TOUCH_ID_ENABLED, false)
                    && !sharedPreferenceManager.getDecryptedPassCode().isNullOrEmpty()
        } else
            false
    }

    override fun login(loginRequest: LoginRequest) {
        launch {
            showLoading(true)
            val response = authApi.login(loginRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        val data = response.data
                        if (!data.accessToken.isNullOrBlank()) {
                            getAccountInfo()
                            hideLoading()
                        } else {
                            createOtp(
                                username = loginRequest.clientId,
                                passcode = loginRequest.clientSecret,
                                deviceId = loginRequest.deviceId
                            )
                        }
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        handleLoginFailed(response.error)
                    }
                }
            }
        }
    }

    private fun createOtp(username: String?, passcode: String?, deviceId: String?) {
        val request = DemographicDataRequest(
            clientId = username,
            clientSecret = passcode,
            deviceId = deviceId
        )
        launch {
            val response = customersApi.generateOTPForDeviceVerification(request)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isDeviceValidate.value = false
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _isDeviceValidate.value = true
                    // Fetch Feature flags
                    getFeatureFlags()
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
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

    private fun handleLoginFailed(error: ApiError) {
        when (error.actualCode) {
            "302" -> {
                val message =
                    resourcesProviders.getString(screen_verify_passcode_text_account_locked)
                blockAccount(errorMessage = message)
            }
            "303" -> {
                viewState.isScreenLocked.value = true
                viewState.isAccountLocked.value = false
                viewState.valid.value = false
                TimeUnit.SECONDS.toMinutes(error.message.toLongOrNull() ?: 0)
                val totalSeconds = error.message.toLongOrNull() ?: 0
                startCountDownTimer(totalSeconds)
            }
            "1260" -> blockAccount(errorMessage = error.message)
            else -> viewState.passcodeError.value = error.message
        }
    }

    override fun blockAccount(errorMessage: String) {
        viewState.passcodeError.value = errorMessage
        viewState.isScreenLocked.value = true
        viewState.isAccountLocked.value = true
        viewState.valid.value = false
    }

    override fun createForgotOtp(forgotPasscodeOtpRequest: ForgotPasscodeOtpRequest) {
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

    fun unlockState() {
        viewState.passcodeError.value = ""
        viewState.isScreenLocked.value = false
        viewState.isAccountLocked.value = false
        viewState.valid.value = true
    }

    override fun logout(accountUUID: String, success: () -> Unit) {
        launch(Dispatcher.Main) {
            showLoading(true)
            sessionManager.doLogOut(accountUUID) { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    hideLoading()
                    success.invoke()
                } else {
                    showAlertMessage(errorMessage)
                    hideLoading()
                }
            }
        }
    }


    override fun openBiometricSettingScreen() {
        _openPermissionSettingsPrivate.value =
            SingleEvent(R.id.action_loginPasscodeFragment_to_biometricPermissionFragment)
    }

    override fun openWaitingListScreen() {
        _openWaitingListPrivate.value = SingleEvent(R.id.waitingListFragment)
    }

    override fun openAllowedUserScreen() {
        _openAllowedUserPrivate.value =
            SingleEvent(R.id.action_loginPasscodeFragment_to_reachedQueueTopFragment)
    }

    override fun openOTPVerificationScreen() {
        _openOTPVerification.value =
            SingleEvent(R.id.action_loginPasscodeFragment_to_loginOTPVerificationFragment)
    }

    override fun openForgotOTPVerificationScreen() {
        _openForgotOTPVerification.value =
            SingleEvent(R.id.action_loginPasscodeFragment_to_forgot_passcode_nav_graph)
    }

    override fun openDashboardScreen() {
        _openDashboardPrivate.value = SingleEvent(sessionManager.userAccount.value)
    }

    private fun startCountDownTimer(totalSeconds: Long) {
        val timer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(totalSeconds), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewState.passcodeError.value =
                    "Too many attempts. Please wait for ${
                        timerString(
                            minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            millisUntilFinished
                                        )
                                    )
                        )
                    }"
            }

            override fun onFinish() {
                unlockState()
            }
        }
        timer.start()
    }

    private fun timerString(minutes: Long, seconds: Long): String {
        return String.format(
            "%02d:%02d",
            minutes,
            seconds
        )
    }

}