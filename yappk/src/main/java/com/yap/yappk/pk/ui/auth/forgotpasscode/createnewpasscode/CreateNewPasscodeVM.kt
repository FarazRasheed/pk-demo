package com.yap.yappk.pk.ui.auth.forgotpasscode.createnewpasscode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.KEY_TOUCH_ID_ENABLED
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.extensions.hasAllSameChars
import com.yap.uikit.utils.extensions.isSequenced
import com.yap.yappk.R
import com.yap.yappk.localization.error_passcode_same_digits
import com.yap.yappk.localization.error_passcode_sequence
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CreateNewPasscodeRequest
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateNewPasscodeVM @Inject constructor(
    override val viewState: CreateNewPasscodeState,
    private val customersApi: CustomersApi,
    override val sharedPreferenceManager: SharedPreferenceManager,
    private val resourcesProviders: ResourcesProviders
) : BaseViewModel<ICreateNewPasscode.State>(), ICreateNewPasscode.ViewModel {
    override var mobileNumber: String = ""
    override var optToken: String? = null

    private val _isPasscodeCreated: MutableLiveData<Boolean> = MutableLiveData()
    override val isPasscodeCreated: LiveData<Boolean> = _isPasscodeCreated

    private val _openCreateNewPasscodeSuccess = MutableLiveData<SingleEvent<Int>>()
    override val openCreateNewPasscodeSuccess: LiveData<SingleEvent<Int>> get() = _openCreateNewPasscodeSuccess

    override fun handlePressOnCreatePasscode() {
        if (isPasscodeValid(viewState.passcode.value ?: "")) {
            viewState.passcodeError.value = ""
        } else {
            viewState.passcodeError.value = getPassCodeError(viewState.passcode.value ?: "")
        }
    }

    override fun savePassCode() {
        sharedPreferenceManager.savePassCodeWithEncryption(viewState.passcode.value ?: "")
    }

    private fun isPasscodeValid(passcode: String): Boolean {
        return !passcode.hasAllSameChars() && !passcode.isSequenced()
    }

    private fun getPassCodeError(passcode: String): String {
        if (passcode.isSequenced()) return resourcesProviders.getString(keyID = error_passcode_sequence)
        if (passcode.hasAllSameChars()) return resourcesProviders.getString(keyID = error_passcode_same_digits)

        return ""
    }

    override fun isBiometricLoginEnabled(isBiometricAvailable: Boolean): Boolean {
        return if (isBiometricAvailable) {
            sharedPreferenceManager.getValueBoolien(KEY_TOUCH_ID_ENABLED, false)
                    && !sharedPreferenceManager.getDecryptedPassCode().isNullOrEmpty()
        } else
            false
    }

    override fun createNewPasscode(createNewPasscodeRequest: CreateNewPasscodeRequest) {
        launch {
            showLoading(true)
            val response = customersApi.createNewPasscode(createNewPasscodeRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isPasscodeCreated.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isPasscodeCreated.value = false
                        viewState.passcodeError.value = response.error.message
                    }
                }
            }
        }
    }

    override fun openCreateNewPasscodeSuccessScreen() {
        _openCreateNewPasscodeSuccess.value =
            SingleEvent(R.id.action_createNewPasscodeFragment_to_newPasscodeSuccessFragment)
    }


}
