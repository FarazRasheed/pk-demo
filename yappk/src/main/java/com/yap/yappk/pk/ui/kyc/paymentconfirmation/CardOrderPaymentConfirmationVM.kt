package com.yap.yappk.pk.ui.kyc.paymentconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CardOrderRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.ui.usecases.CreateTopUpCheckOutSessionUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardOrderPaymentConfirmationVM @Inject constructor(
    override val viewState: CardOrderPaymentConfirmationState,
    private val createTopUpCheckOutSessionUC: CreateTopUpCheckOutSessionUC,
    override val sessionManager: SessionManager,
    private val transactionsApi: TransactionsApi,
    private val cardsApi: CardsApi
) :
    BaseViewModel<ICardOrderPaymentConfirmation.State>(), ICardOrderPaymentConfirmation.ViewModel {
    private val _check3dSecureSuccess = MutableLiveData<Check3DEnrollmentSession?>()
    override val check3dSecureSuccess: LiveData<Check3DEnrollmentSession?> = _check3dSecureSuccess

    private val _secureId: MutableLiveData<String?> = MutableLiveData()
    override val secureId: LiveData<String?> = _secureId

    private val _orderId: MutableLiveData<String?> = MutableLiveData()
    override val orderId: LiveData<String?> = _orderId

    private val _poolingSuccess = MutableLiveData<Boolean>()
    override val poolingSuccess: LiveData<Boolean> = _poolingSuccess

    private val _topUpSuccess = MutableLiveData<Boolean>()
    override val topUpSuccess: LiveData<Boolean> = _topUpSuccess

    private val _openCardOrderSuccess: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openCardOrderSuccess: LiveData<SingleEvent<Int>> = _openCardOrderSuccess

    private val _openValidCustomer: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openValidCustomer: LiveData<SingleEvent<Int>> = _openValidCustomer

    private val _openInValidCustomer: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openInValidCustomer: LiveData<SingleEvent<Int>> = _openInValidCustomer


    private val _accountInfo = MutableLiveData<AccountInfo?>()
    override val accountInfo: LiveData<AccountInfo?> = _accountInfo

    private var _address: MutableLiveData<CardOrderRequest> = MutableLiveData()
    override val address: LiveData<CardOrderRequest> = _address

    private val _isCardOrdered = MutableLiveData<AccountInfo?>()
    override val isCardOrdered: LiveData<AccountInfo?> = _isCardOrdered

    override fun setAddress(address: CardOrderRequest?) {
        _address.value = address
    }

    override fun setSecureId(secureId: String?) {
        _secureId.value = secureId
    }

    override fun setOrderId(orderId: String?) {
        _orderId.value = orderId
    }

    override fun createTopUpSessionUseCase(
        sessionId: String?,
        beneficiaryId: Int?,
        currency: String,
        amount: String?
    ) {
        showLoading()
        createTopUpCheckOutSessionUC.executeUseCase(
            CreateTopUpCheckOutSessionUC.RequestValues(
                sessionId = sessionId,
                beneficiaryId = beneficiaryId,
                currency = currency,
                amount = amount
            ),
            object :
                UseCaseCallback<CreateTopUpCheckOutSessionUC.ResponseValue, CreateTopUpCheckOutSessionUC.ErrorValue> {
                override fun onSuccess(response: CreateTopUpCheckOutSessionUC.ResponseValue) {
                    handleUseCaseResponse(response)
                    hideLoading()
                }

                override fun onError(error: CreateTopUpCheckOutSessionUC.ErrorValue) {
                    hideLoading()
                }
            })
    }

    override fun topUpTransactionRequest(
        beneficiaryId: String?,
        secureId: String,
        orderId: String,
        currency: String,
        amount: String,
        securityCode: String?,
        sessionId: String?
    ) {
        launch {
            showLoading(true)
            val response =
                transactionsApi.onBoardingCardTopUpTransactionRequest(
                    beneficiaryId = beneficiaryId,
                    sessionId = sessionId,
                    secureId = secureId,
                    orderId = orderId,
                    currency = currency,
                    amount = amount,
                    securityCode = securityCode
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _topUpSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showToast(response.error.message)
                    }
                }
            }
        }
    }

    override fun openCardOrderSuccessScreen() {
        _openCardOrderSuccess.value =
            SingleEvent(R.id.action_cardOrderPaymentConfirmationFragment_to_cardOrderSuccessFragment)
    }

    override fun openValidCustomerScreen() {
        _openInValidCustomer.value =
            SingleEvent(R.id.action_cardOrderPaymentConfirmationFragment_to_validCustomerFragment)
    }

    override fun openInValidCustomerScreen() {
        _openInValidCustomer.value =
            SingleEvent(R.id.action_cardOrderPaymentConfirmationFragment_to_invalidCustomerFragment)
    }

    override fun startPooling(secureId: String?) {
        showLoading()
        launch {
            val response =
                transactionsApi.secureIdPooling(
                    secureId = secureId
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        when (response.data.data) {
                            null -> {
                                delay(3000)
                                _poolingSuccess.value = false
                                hideLoading()
                            }
                            "N" -> {
                                showToast("unable to verify")
                                hideLoading()
                            }
                            "Y" -> {
                                _poolingSuccess.value = true
                                hideLoading()
                            }
                        }
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun saveAddressCard(
        cardOrderRequest: CardOrderRequest
    ) {
        launch {
            showLoading(onBackGround = true)
            val response = cardsApi.saveAddressAndCreateCard(cardOrderRequest = cardOrderRequest)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _accountInfo.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _accountInfo.value = null
                    }
                }
            }
        }
    }

    override fun orderCard(cardFee: String, cardScheme: String) {
        launch {
            showLoading(onBackGround = true)
            val response = cardsApi.orderPhysicalCard(cardFee = cardFee, cardScheme = cardScheme)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isCardOrdered.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _isCardOrdered.value = null
                    }
                }
            }
        }
    }

    private fun handleUseCaseResponse(response: CreateTopUpCheckOutSessionUC.ResponseValue) {
        _check3dSecureSuccess.value = response.check3dEnrollment
        setOrderId(response.orderId)
    }

}
