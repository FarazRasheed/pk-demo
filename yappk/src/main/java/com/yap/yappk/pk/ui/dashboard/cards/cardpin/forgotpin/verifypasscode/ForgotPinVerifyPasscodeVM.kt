package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.verifypasscode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ForgotPinVerifyPasscodeVM @Inject constructor(
    override val viewState: ForgotPinVerifyPasscodeState,
    private val customersApi: CustomersApi
) : BaseViewModel<IForgotPinVerifyPasscode.State>(), IForgotPinVerifyPasscode.ViewModel {

    private val _openEnterNewCardPinScreen = MutableLiveData<SingleEvent<Int>>()
    override val openEnterNewCardPinScreen: LiveData<SingleEvent<Int>> = _openEnterNewCardPinScreen

    private val _isPasscodeVerified = MutableLiveData<Boolean>()
    override val isPasscodeVerified: LiveData<Boolean> = _isPasscodeVerified

    override fun handlePressOnVerify() {
            verifyPasscode(viewState.passcode.value ?: "")
    }

    override fun openEnterNewCardPinScreen() {
        _openEnterNewCardPinScreen.value =
            SingleEvent(R.id.action_forgotPinVerifyPasscodeFragment_to_forgotPinEnterNewCardPinFragment)
    }

    override fun verifyPasscode(passcode: String) {
        launch {
            showLoading(true)
            val response = customersApi.verifyPasscode(passcode)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isPasscodeVerified.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isPasscodeVerified.value = false
                        viewState.passcodeError.value = response.error.message
                    }
                }
            }
        }
    }
}
