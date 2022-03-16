package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.uikit.utils.extensions.parseToDouble
import com.yap.yappk.localization.common_display_text_available_balance_error
import com.yap.yappk.localization.common_display_text_daily_limit_error_single_transaction
import com.yap.yappk.localization.common_sm_display_text_min_max_limit_error_transaction
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.AccountBalance
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferLimits
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionFeeResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.YapToYapTransaction
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.utils.enums.TransactionProductCode
import com.yap.yappk.pk.utils.isGreaterThan
import com.yap.yappk.pk.utils.isLessThan
import com.yap.yappk.pk.utils.isValueWithinRange
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class YapToYapTransferVM @Inject constructor(
    override val viewState: YapToYapTransferState,
    private val transactionsApi: TransactionsApi,
    private val customersApi: CustomersApi,
    override val sessionManager: SessionManager,
    private val resourcesProviders: ResourcesProviders,
    override val sharedPreferenceManager: SharedPreferenceManager
) :
    BaseViewModel<IYapToYapTransfer.State>(), IYapToYapTransfer.ViewModel {
    override var transactionThreshold: MutableLiveData<TransactionThresholdResponse>? = MutableLiveData()

    private val _y2yTransferSuccess = MutableLiveData<YapToYapTransaction>()
    override val y2yTransferSuccess: LiveData<YapToYapTransaction> = _y2yTransferSuccess

    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

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

            (enteredAmount.plus(viewState.transferFee.value.parseToDouble())).isGreaterThan(
                (viewState.accountCurrentBalance?.value.parseToDouble())
            ) -> {
                viewState.isValidAmount.value = false
                resourcesProviders.getString(
                    keyID = common_display_text_available_balance_error,
                    enteredAmount.toString().toFormattedCurrency()
                )
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

    override fun getCombinedApisParallel() {
        showLoading()
        fetchAllApis { feeResponse, fundTransferLimitsResponse, transactionThresholdResponse, accountBalanceResponse ->
            launch(Dispatcher.Main) {
                when (feeResponse) {
                    is ApiResponse.Success -> {
                        viewState.transferFee.value = feeResponse.data.data?.fixedAmount.toString()
                    }
                    is ApiResponse.Error -> {

                    }
                }
                when (fundTransferLimitsResponse) {
                    is ApiResponse.Success -> {
                        viewState.minLimit?.value = fundTransferLimitsResponse.data.data?.minLimit.parseToDouble()
                        viewState.maxLimit?.value = fundTransferLimitsResponse.data.data?.maxLimit.parseToDouble()
                    }
                    is ApiResponse.Error -> {

                    }
                }
                when (transactionThresholdResponse) {
                    is ApiResponse.Success -> {
                        transactionThreshold?.value = transactionThresholdResponse.data.data
                    }
                    is ApiResponse.Error -> {

                    }
                }

                when (accountBalanceResponse) {
                    is ApiResponse.Success -> {
                        viewState.accountCurrentBalance?.value =
                            accountBalanceResponse.data.data?.currentBalance.toString().toFormattedCurrency()
                    }
                    is ApiResponse.Error -> {

                    }
                }
                hideLoading()
                viewState.isFocusable.value = true
            }

        }
    }

    override fun y2yTransferRequest(
        receiverUUID: String?,
        beneficiaryName: String?,
        deviceId: String?,
        amount: String?,
        otpVerificationReq: Boolean?,
        remarks: String?
    ) {
        launch {
            showLoading(true)
            val response =
                transactionsApi.y2yFundsTransferRequest(
                    receiverUUID,
                    beneficiaryName,
                    deviceId,
                    amount,
                    otpVerificationReq,
                    remarks
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _y2yTransferSuccess.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _errorDescription.value = response.error.message
                    }
                }
            }
        }
    }

    override fun fetchAllApis(
        responses: (
            ApiResponse<BaseResponse<TransactionFeeResponse>>?, ApiResponse<BaseResponse<FundTransferLimits>>?, ApiResponse<BaseResponse<TransactionThresholdResponse>>?, ApiResponse<BaseResponse<AccountBalance>>
        ) -> Unit,
    ) {
        launch {
            val deferredFeeResponse = launchAsync {
                transactionsApi.getTransactionFeeWithProductCode(
                    TransactionProductCode.Y2Y_TRANSFER.pCode
                )
            }
            val deferredFundTransferLimitsResponse =
                launchAsync { transactionsApi.getFundTransferLimits(TransactionProductCode.Y2Y_TRANSFER.pCode) }
            val deferredTransactionThresholdResponse = launchAsync { transactionsApi.getTransactionThresholds() }
            val deferredAccountBalanceResponse = launchAsync {
                customersApi.getAccountBalance()
            }
            responses(
                deferredFeeResponse.await(),
                deferredFundTransferLimitsResponse.await(),
                deferredTransactionThresholdResponse.await(),
                deferredAccountBalanceResponse.await()
            )
        }
    }

}