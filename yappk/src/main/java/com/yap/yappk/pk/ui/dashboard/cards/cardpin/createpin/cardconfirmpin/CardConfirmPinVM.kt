package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.localization.error_pin_code_not_same_digits
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.requestdtos.CardPinRequest
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardConfirmPinVM @Inject constructor(
    override val viewState: CardConfirmPinState,
    private val resourcesProviders: ResourcesProviders,
    private val cardsApi: CardsApi
) :
    BaseViewModel<ICardConfirmPin.State>(), ICardConfirmPin.ViewModel {
    override var confirmPin: String = ""

    private val _openCardPinSuccess = MutableLiveData<SingleEvent<Int>>()
    override val openCardPinSuccess: LiveData<SingleEvent<Int>> get() = _openCardPinSuccess

    private val _isPinCodeSet = MutableLiveData<Boolean>()
    override val isPinCodeSet: LiveData<Boolean> get() = _isPinCodeSet

    override fun handlePressOnCreate() {
        if (isPinCodeMatched(viewState.newPin.value ?: "", confirmPin)) {
            viewState.pinError.value = ""
        } else {
            viewState.pinError.value = getPinCodeError(viewState.newPin.value ?: "", confirmPin)
        }
    }

    private fun isPinCodeMatched(pinCode: String, pinEntered: String): Boolean {
        return pinCode == pinEntered
    }

    private fun getPinCodeError(pinCode: String, pinEntered: String): String {
        if (pinCode != pinEntered) return resourcesProviders.getString(keyID = error_pin_code_not_same_digits)
        return ""
    }

    override fun openCardPinSuccessScreen() {
        _openCardPinSuccess.value =
            SingleEvent(R.id.action_cardConfirmPinFragment_to_cardPinSuccessFragment)
    }

    override fun getCardPinRequest(): CardPinRequest {
        return CardPinRequest(cardPin = viewState.newPin.value)
    }

    override fun setCardPin(cardPin: String, cardSerialNumber: String) {
        launch {
            showLoading(true)
            val response = cardsApi.setCardPin(cardPin, cardSerialNumber)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isPinCodeSet.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isPinCodeSet.value = false
                        viewState.pinError.value = response.error.message
                    }
                }
            }
        }
    }

}
