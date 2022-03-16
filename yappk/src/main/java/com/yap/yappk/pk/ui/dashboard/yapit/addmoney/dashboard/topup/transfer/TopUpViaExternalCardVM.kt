package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.utils.extensions.parseToDouble
import com.yap.yappk.localization.common_display_text_daily_limit_error_single_transaction
import com.yap.yappk.localization.common_sm_display_text_min_max_limit_error_transaction
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.Check3DEnrollmentSession
import com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession.CreateSession
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.adapter.DenominationsAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.usecases.FundsTransferAPIUC
import com.yap.yappk.pk.utils.enums.TransactionProductCode
import com.yap.yappk.pk.utils.isGreaterThan
import com.yap.yappk.pk.utils.isLessThan
import com.yap.yappk.pk.utils.isValueWithinRange
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TopUpViaExternalCardVM @Inject constructor(
    override val viewState: TopUpViaExternalCardState,
    private val fundsTransferAPIUC: FundsTransferAPIUC,
    private val transactionsApi: TransactionsApi,
    private val resourcesProviders: ResourcesProviders,
    override val sessionManager: SessionManager
) :
    BaseViewModel<ITopUpViaExternalCard.State>(), ITopUpViaExternalCard.ViewModel {
    override var transactionThreshold: MutableLiveData<TransactionThresholdResponse>? = MutableLiveData()
    override val denominationsAdapter: DenominationsAdapter =
        DenominationsAdapter(arrayListOf(), resourcesProviders)

    private val _externalCard: MutableLiveData<ExternalCard?> = MutableLiveData()
    override val externalCard: LiveData<ExternalCard?> = _externalCard

    private val _secureId: MutableLiveData<String?> = MutableLiveData()
    override val secureId: LiveData<String?> = _secureId

    private val _orderId: MutableLiveData<String?> = MutableLiveData()
    override val orderId: LiveData<String?> = _orderId

    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

    private val _checkOutSessionResponse = MutableLiveData<CreateSession>()
    override val checkOutSessionResponse: LiveData<CreateSession> = _checkOutSessionResponse

    private val _check3dSecureSuccess = MutableLiveData<Check3DEnrollmentSession>()
    override val check3dSecureSuccess: LiveData<Check3DEnrollmentSession> = _check3dSecureSuccess

    private val _poolingSuccess = MutableLiveData<Boolean>()
    override val poolingSuccess: LiveData<Boolean> = _poolingSuccess

    override fun setCard(card: ExternalCard?) {
        _externalCard.value = card
    }

    override fun setSecureId(secureId: String?) {
        _secureId.value = secureId
    }

    override fun setOrderId(orderId: String?) {
        _orderId.value = orderId
    }

    override fun onAmountChange(
        amount: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValidAmount.value = false
        val enteredAmount = amount.parseToDouble()
        viewState.isValidAmount.value = enteredAmount.isGreaterThan(0.0)
        val remainingDailyLimit: Double =
            transactionThreshold?.value?.dailyLimit?.minus(transactionThreshold?.value?.totalDebitAmount ?: 0.0)
                ?: 0.0
        _errorDescription.value = when {
            enteredAmount <= 0 -> {
                ""
            }

            remainingDailyLimit.isLessThan(0.0) -> {
                viewState.isValidAmount.value = false
                resourcesProviders.getString(
                    keyID = common_display_text_daily_limit_error_single_transaction
                )
            }

            enteredAmount.isValueWithinRange(viewState.minLimit?.value, viewState.maxLimit?.value) -> {
                viewState.isValidAmount.value = false
                resourcesProviders.getString(
                    keyID = common_sm_display_text_min_max_limit_error_transaction,
                    viewState.minLimit?.value.toString().toFormattedCurrency(),
                    viewState.maxLimit?.value.toString().toFormattedCurrency()
                )
            }

            else -> {
                viewState.isValidAmount.value = true
                ""
            }
        }
    }

    /*
    * Below function covers all the apis that needed to show the data on screen like transaction limits, available balance
    * */
    override fun fetchAllInitialApis() {
        callApiUseCase()
    }

    override fun callApiUseCase() {
        showLoading()
        fundsTransferAPIUC.executeUseCase(
            FundsTransferAPIUC.RequestValues(TransactionProductCode.TOPUP_VIA_EXTERNAL_CARD.pCode),
            object :
                UseCaseCallback<FundsTransferAPIUC.ResponseValue, FundsTransferAPIUC.ErrorValue> {
                override fun onSuccess(response: FundsTransferAPIUC.ResponseValue) {
                    handleUseCaseResponse(response)
                    hideLoading()
                }

                override fun onError(error: FundsTransferAPIUC.ErrorValue) {
                    _errorDescription.value = error.msg
                    hideLoading()
                }
            })
    }

    private fun handleUseCaseResponse(response: FundsTransferAPIUC.ResponseValue) {
        viewState.transferFee.value = (response.transactionFeeResponse?.fixedAmount ?: 0.00).toString()
        viewState.minLimit?.value = response.fundTransferLimitsResponse?.minLimit.parseToDouble()
        viewState.maxLimit?.value = response.fundTransferLimitsResponse?.maxLimit.parseToDouble()
        transactionThreshold?.value = response.transactionThresholdResponse
        viewState.accountCurrentBalance?.value =
            response.accountBalanceResponse?.currentBalance.toString().toFormattedCurrency()
        denominationsAdapter.setList(response.denominationsResponse ?: arrayListOf())
        viewState.isFocusable.value = true
    }

    override fun createTransactionSession(currency: String?, amount: String?) {
        launch {
            showLoading(true)
            val response =
                transactionsApi.createTransactionSession(currency = currency, amount = amount)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _checkOutSessionResponse.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        _errorDescription.value = response.error.message
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun check3DEnrollmentSessionRequest(
        beneficiaryId: Int?,
        orderId: String?,
        sessionId: String?,
        currency: String?,
        amount: String?
    ) {
        launch {
            val response =
                transactionsApi.check3DEnrollmentSession(
                    beneficiaryId = beneficiaryId,
                    orderId = orderId,
                    sessionId = sessionId, currency, amount
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _check3dSecureSuccess.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        _errorDescription.value = response.error.message
                    }
                }
                hideLoading()
            }
        }
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
                        _errorDescription.value = response.error.message
                    }
                }
            }
        }
    }
}