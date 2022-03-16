package com.digitify.testyappakistan.onboarding.mobile

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.digitify.testyappakistan.configurations.SuperAppConfigManager
import com.digitify.testyappakistan.configurations.UserVerifierProvider
import com.yap.yappk.pk.utils.PhoneUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MobileNoVM @Inject constructor(
    private val phoneUtils: PhoneUtils,
    private val loadConfig: SuperAppConfigManager
) :
    BaseViewModel<IMobileNo.State>(),
    IMobileNo.ViewModel {
    override val viewState: IMobileNo.State = MobileNoState()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showErrorPrivate = MutableLiveData<SingleEvent<String?>>()
    override val showError: LiveData<SingleEvent<String?>> get() = showErrorPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _otpCreateEvent = MutableLiveData<Boolean>()
    override var otpCreateEvent: LiveData<Boolean> = _otpCreateEvent

    private val userVerifier: UserVerifierProvider = UserVerifierProvider()

    fun onPhoneNumberTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        val countryCodeWithoutPlus = viewState.countryCode.get()?.replace("+", "")?.toInt()
        showErrorPrivate.value = SingleEvent(null)
        viewState.isValid.value =
            phoneUtils.isValidPhoneNumber(
                s.toString(),
                phoneUtils.getCountryCodeForRegion(countryCodeWithoutPlus ?: 92)
            )
    }

    override fun createOtpUserVerifier(countryCode: String, mobileNumber: String) {
        showLoading()
        loadConfig.initYapRegion(countryCode)
        userVerifier.provideCreateOtpVerifier(countryCode)
            .createOtp(countryCode, mobileNumber) { result ->
                if (result.isSuccess && result.getOrNull() == true) {
                    _otpCreateEvent.value = result.getOrNull() ?: false
                    hideLoading()
                } else {
                    result.onFailure {
                        showErrorPrivate.value = SingleEvent(it.message ?: "")
                        _otpCreateEvent.value = false
                        hideLoading()
                    }
                }
            }
    }
}