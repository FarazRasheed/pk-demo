package com.digitify.testyappakistan.signin.login

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
class LoginVM @Inject constructor(
    override val viewState: LoginState,
    private val phoneUtils: PhoneUtils,
    private val loadConfig: SuperAppConfigManager
) : BaseViewModel<ILogin.State>(), ILogin.ViewModel {
    private val _verifyUserEvent: MutableLiveData<Boolean> = MutableLiveData()
    override val verifyUserEvent: LiveData<Boolean> = _verifyUserEvent

    private val _isAccountBlocked: MutableLiveData<Boolean> = MutableLiveData()
    override val isAccountBlocked: LiveData<Boolean> = _isAccountBlocked

    private val _openAuthentication = MutableLiveData<SingleEvent<String>>()
    override val openAuthentication: LiveData<SingleEvent<String>> get() = _openAuthentication

    private val userVerifier: UserVerifierProvider = UserVerifierProvider()

    fun onPhoneNumberTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isError.set(false)
        viewState.isValid.value =
            phoneUtils.isValidPhoneNumber(
                s.toString(),
                phoneUtils.getCountryCodeForRegion(
                    viewState.countryCode.get().toString().replace("+", "").toInt()
                )
            )
    }

    override fun verifyUser(countryCode: String?, mobileNo: String?) {
        showLoading()
        loadConfig.initYapRegion(countryCode)
        userVerifier.provide(countryCode).verifyUser(mobileNo ?: "") { result ->
            if (result.isSuccess) {
                _verifyUserEvent.value = result.getOrNull()
                hideLoading()
            } else {
                result.onFailure {
                    handleBlockedAccountError(it.message ?: "")
                    hideLoading()
                }
            }
        }
    }

    private fun handleBlockedAccountError(error: String) {
        when (error) {
            "AD-10018" -> {
                _isAccountBlocked.value = true
            }
            else -> {
                _verifyUserEvent.value = false
            }
        }
    }

    override fun openAuthenticationScreen() {
        _openAuthentication.value = SingleEvent(viewState.countryCode.get() ?: "")
    }
}