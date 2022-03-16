package com.yap.yappk.pk.ui.kyc.cardname.cardnameconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardNameConfirmationVM @Inject constructor(
    override val viewState: CardNameConfirmationState,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) : BaseViewModel<ICardNameConfirmation.State>(), ICardNameConfirmation.ViewModel {
    private val _isCardNameSaved: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardNameSaved: LiveData<Boolean> = _isCardNameSaved

    private val _openCardPaymentAddCard = MutableLiveData<SingleEvent<Int>>()
    override val openCardPaymentAddCard: LiveData<SingleEvent<Int>> get() = _openCardPaymentAddCard

    private val _openAddressConfirmation = MutableLiveData<SingleEvent<Int>>()
    override val openAddressConfirmation: LiveData<SingleEvent<Int>> get() = _openAddressConfirmation

    private val _openCardDashBoard = MutableLiveData<SingleEvent<Int>>()
    override val openCardDashBoard: LiveData<SingleEvent<Int>> get() = _openCardDashBoard

    override fun saveCardName(cardName: String) {
        launch {
            showLoading(true)
            val response = customersApi.saveCardName(cardName)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isCardNameSaved.value = false
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
                    _isCardNameSaved.value = true
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
                    _isCardNameSaved.value = false
                }
            }
        }
    }

    override fun openAddCardScreen() {
        _openCardPaymentAddCard.value =
            SingleEvent(R.id.action_cardNameConfirmationFragment_to_cardPaymentAddCardFragment)
    }

    override fun openAddressScreen() {
        _openAddressConfirmation.value =
            SingleEvent(R.id.action_cardNameConfirmationFragment_to_addressSelectionFragment)
    }

    override fun openCardDashboardScreen() {
        _openCardDashBoard.value =
            SingleEvent(R.id.action_cardNameConfirmationFragment_to_cardPaymentDashboardFragment)
    }
}