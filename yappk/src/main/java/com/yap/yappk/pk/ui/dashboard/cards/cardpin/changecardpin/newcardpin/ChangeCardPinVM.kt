package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.newcardpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.IPasscodeValidator
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType.SCREEN_TYPE_CHANGE_PIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangeCardPinVM @Inject constructor(
    override val viewState: ChangeCardPinState,
    private val cardsApi: CardsApi,
    private val passCodeValidator: IPasscodeValidator
) :
    BaseViewModel<IChangeCardPin.State>(), IChangeCardPin.ViewModel {

    private val _openConfirmCardPin = MutableLiveData<SingleEvent<Int>>()
    override val openConfirmCardPin: LiveData<SingleEvent<Int>> = _openConfirmCardPin

    private val _changePinSuccess = MutableLiveData<Boolean>()
    override val changePinSuccess: LiveData<Boolean> = _changePinSuccess

    override fun checkPasscodeValidation() {
        if (viewState.screenType.value == SCREEN_TYPE_CHANGE_PIN.name) {
            val passcodeError = passCodeValidator.validatePasscode(viewState.pin.value)
            viewState.pinError.value = if (passcodeError.isNullOrBlank()) "" else passcodeError
        } else {
            val confirmPasscodeError = passCodeValidator.validateConfirmPasscode(
                passcode = viewState.pin.value,
                confirmPasscode = viewState.changeCardPinModel.value?.cardConfirmPin ?: ""
            )
            viewState.pinError.value = if (confirmPasscodeError.isNullOrBlank()) "" else confirmPasscodeError
        }
    }

    override fun navigateToConfirmCardPinScreen() {
        _openConfirmCardPin.value = SingleEvent(R.id.action_changeCardPinFragment_self)
    }

    override fun navigateToChangeCardPinSuccessScreen() {
        _openConfirmCardPin.value =
            SingleEvent(R.id.action_changeCardPinFragment_to_changeCardPinSuccessFragment)
    }

    override fun changeCardPin(
        oldPin: String,
        newPin: String,
        confirmPin: String,
        cardSerialNumber: String
    ) {
        launch {
            showLoading(true)
            val response =
                cardsApi.changeCardPin(oldPin, newPin, confirmPin, cardSerialNumber)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _changePinSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        viewState.pinError.value = response.error.message
                    }
                }
            }
        }
    }
}
