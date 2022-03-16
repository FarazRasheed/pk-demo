package com.yap.yappk.pk.ui.kyc.cardorder.cardpaymentaddcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CardHostedFlavour
import com.yap.yappk.pk.ui.kyc.enums.PaymentCardHostedPageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardPaymentAddCardVM @Inject constructor(
    override val viewState: CardPaymentAddCardState,
    override val pkBuildConfigurations: PKBuildConfigurations,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) : BaseViewModel<ICardPaymentAddCard.State>(), ICardPaymentAddCard.ViewModel {
    private val _card: MutableLiveData<ExternalCard> = MutableLiveData()
    override val card: LiveData<ExternalCard> = _card

    private val _openAddressConfirmation = MutableLiveData<SingleEvent<Int>>()
    override val openAddressConfirmation: LiveData<SingleEvent<Int>> get() = _openAddressConfirmation

    override fun getAddCardPageUrl(buildFlavour: String): String {
        return when (buildFlavour) {
            CardHostedFlavour.DEV.flavour -> {
                PaymentCardHostedPageUrl.DEV.url
            }
            CardHostedFlavour.QA.flavour -> {
                PaymentCardHostedPageUrl.QA.url
            }
            CardHostedFlavour.STG.flavour -> {
                PaymentCardHostedPageUrl.STG.url
            }
            else -> ""
        }
    }

    override fun addExternalCard(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.addExternalCardPayment(
                sessionId = sessionId,
                cardAlias = cardAlias,
                cardColor = cardColor,
                cardNumber = cardNumber
            )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        val card = response.data.data
                        card?.also { it.sessionId = sessionId }
                        _card.value = card
                        hideLoading()
                        sessionManager.getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _card.value = null
                    }
                }
            }
        }
    }

    override fun setExternalCard(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ) {
        _card.value = ExternalCard(
            sessionId = sessionId,
            alias = cardAlias,
            color = cardColor,
            number = cardNumber
        )
    }

    override fun openAddressScreen() {
        _openAddressConfirmation.value =
            SingleEvent(R.id.action_cardPaymentAddCardFragment_to_addressSelectionFragment)
    }
}