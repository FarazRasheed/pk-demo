package com.yap.yappk.pk.ui.onboarding.email

import android.os.Bundle
import android.util.Patterns
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.KEY_IS_USER_LOGGED_IN
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.EmailVerificationRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.SaveReferralRequest
import com.yap.yappk.networking.microservices.customers.requestsdtos.SignUpRequest
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.featureflag.PKFeatureFlagClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EmailVerificationVM @Inject constructor(
    override val viewState: EmailVerificationState,
    private val customersApi: CustomersApi,
    override val sharedPreferenceManager: SharedPreferenceManager,
    override val sessionManager: SessionManager,
    private var pkFeatureFlags: PKFeatureFlagClient
) : BaseViewModel<IEmailVerification.State>(), IEmailVerification.ViewModel {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _savProfileEvent: MutableLiveData<Boolean> = MutableLiveData()
    override val savProfileEvent: LiveData<Boolean> = _savProfileEvent

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _animationStartEvent: MutableLiveData<Boolean> = MutableLiveData()
    override val animationStartEvent: LiveData<Boolean> = _animationStartEvent

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showErrorPrivate = MutableLiveData<SingleEvent<String?>>()
    override val showError: LiveData<SingleEvent<String?>> get() = showErrorPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _openSignupSuccess = MutableLiveData<SingleEvent<Bundle>>()
    override val openSignupSuccess: LiveData<SingleEvent<Bundle>> = _openSignupSuccess

    fun onEmailTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        showErrorPrivate.value = SingleEvent(null)
        val emailPattern = Patterns.EMAIL_ADDRESS
        viewState.isValid.value = emailPattern.matcher(s.toString()).matches()
    }

    override fun verifyEmailAndSignup(
        emailVerificationRequest: EmailVerificationRequest,
        signUpRequest: SignUpRequest,
        postDataRequest: DemographicDataRequest
    ) {
        launch {
            showLoading(true)
            emailVerification(emailVerificationRequest) { token ->
                signUpRequest.token = token
                saveProfile(signUpRequest) {
                    sharedPreferenceManager.getReferralInfo()?.let {
                        requestSaveReferral(SaveReferralRequest(it.id, it.date))
                    }
                    postDemographicData(postDataRequest) {
                        getAccountInfo()
                    }
                }
            }
        }
    }

    private fun emailVerification(
        emailVerificationRequest: EmailVerificationRequest,
        success: (token: String) -> Unit
    ) {
        launch {
            val response = customersApi.emailVerification(emailVerificationRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        success.invoke(response.data.data ?: "")
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showErrorMessage(response.error.message)
                        _savProfileEvent.value = false
                    }
                }
            }
        }
    }

    private fun saveProfile(signUpRequest: SignUpRequest, success: () -> Unit) {
        launch {
            val response = customersApi.signUp(signUpRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        saveUserProfileLocally(
                            signUpRequest.passcode,
                            signUpRequest.mobileNo,
                            signUpRequest.countryCode
                        )
                        success.invoke()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showErrorMessage(response.error.message)
                        _savProfileEvent.value = false
                    }
                }
            }
        }
    }

    private fun postDemographicData(
        demographicDataRequest: DemographicDataRequest,
        success: () -> Unit
    ) {
        launch {
            val response = customersApi.postDemographicData(demographicDataRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        success.invoke()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showErrorMessage(response.error.message)
                        _savProfileEvent.value = false
                    }
                }
            }
        }
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _savProfileEvent.value = true
                    getFeatureFlags()
                    hideLoading()
                } else {
                    hideLoading()
                    showErrorMessage(errorMessage)
                    _savProfileEvent.value = false
                }
            }
        }
    }

    // Fetch Feature flags
    private fun getFeatureFlags(){
        launch {
            // right now there no Api involved to fetch feature flags
            pkFeatureFlags.getLocalFeatureFlags()
        }
    }

    private fun requestSaveReferral(saveReferralRequest: SaveReferralRequest) {
        launch {
            when (val response = customersApi.saveReferralInvitation(saveReferralRequest)) {
                is ApiResponse.Success -> {
                    sharedPreferenceManager.setReferralInfo(null)
                }
                is ApiResponse.Error -> {
                    showAlertMessage(response.error.message, true)
                }
            }
        }
    }

    private fun saveUserProfileLocally(passcode: String?, mobileNo: String?, countryCode: String?) {
        sharedPreferenceManager.save(
            KEY_IS_USER_LOGGED_IN,
            true
        )
        sharedPreferenceManager.savePassCodeWithEncryption(
            passcode ?: ""
        )
        sharedPreferenceManager.saveUserNameWithEncryption(
            mobileNo ?: ""
        )
        sharedPreferenceManager.saveUserDialCodeWithEncryption(
            countryCode?.replace("00", "+") ?: ""
        )
    }

    override fun openSignupSuccessScreen() {
        val bundle =
            if (sessionManager.userAccount.value?.isWaiting == true) bundleOf("IsWaiting" to true) else bundleOf(
                "IsWaiting" to false
            )
        _openSignupSuccess.value = SingleEvent(bundle)
    }

    private fun showErrorMessage(errorMessage: String) {
        showErrorPrivate.value = SingleEvent(errorMessage)
    }
}