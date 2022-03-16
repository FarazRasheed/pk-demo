package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.currentcardpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.requestdtos.VerifyCardPinRequest
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.IPasscodeValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrentCardPinVM @Inject constructor(
    override val viewState: CurrentCardPinState,
    private val cardsApi: CardsApi
) :
    BaseViewModel<ICurrentCardPin.State>(), ICurrentCardPin.ViewModel {
    private val _openChangeCardPin = MutableLiveData<SingleEvent<Int>>()
    override val openChangeCardPin: LiveData<SingleEvent<Int>> = _openChangeCardPin

    private val _openForgotPin = MutableLiveData<SingleEvent<Int>>()
    override val openForgotPin: LiveData<SingleEvent<Int>> = _openForgotPin

    private val _verifyCardPinSuccess = MutableLiveData<Boolean>()
    override val verifyCardPinSuccess: LiveData<Boolean> = _verifyCardPinSuccess

    override fun navigateToEnterNewPinScreen() {
        _openChangeCardPin.value =
            SingleEvent(R.id.action_currentCardPinFragment_to_changeCardPinFragment)
    }

    override fun navigateToForgotPinScreen() {
        _openForgotPin.value =
            SingleEvent(R.id.action_currentCardPinFragment_to_pk_forgot_card_pin_nav_graph)
    }

    override fun verifyCardPin(cardPin: String, cardSerialNumber: String) {
        launch {
            showLoading(true)
            val response =
                cardsApi.verifyCardPin(cardSerialNumber, VerifyCardPinRequest(cardPin = cardPin))
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _verifyCardPinSuccess.value = true
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

