package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.utils.extensions.parseToDouble
import com.yap.yappk.localization.common_display_text_available_balance_error
import com.yap.yappk.localization.common_display_text_daily_limit_error_single_transaction
import com.yap.yappk.localization.common_sm_display_text_min_max_limit_error_transaction
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferReasons
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import com.yap.yappk.pk.ui.dashboard.yapit.usecases.BankTransferAPIUC
import com.yap.yappk.pk.utils.enums.TransactionProductCode
import com.yap.yappk.pk.utils.isGreaterThan
import com.yap.yappk.pk.utils.isLessThan
import com.yap.yappk.pk.utils.isValueWithinRange
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankTransferVM @Inject constructor(
    override val viewState: BankTransferState,
    private val resourcesProviders: ResourcesProviders,
    private val bankTransferAPIUC: BankTransferAPIUC,
) :
    BaseViewModel<IBankTransfer.State>(), IBankTransfer.ViewModel {
    private val _beneficiary: MutableLiveData<BankTransferDataModel> = MutableLiveData()
    override val beneficiary: LiveData<BankTransferDataModel> = _beneficiary
    override var transactionThreshold: MutableLiveData<TransactionThresholdResponse>? = MutableLiveData()
    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription
    override val reasonsList: MutableLiveData<ArrayList<FundTransferReasons?>> = MutableLiveData()

    //    override var reasonsCategories: Map<String?, List<FundTransferReasons>>? = HashMap()
    override fun setBeneficiary(beneficiary: BankTransferDataModel?) {
        _beneficiary.value = beneficiary
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

    /*
    * Below function covers all the apis that needed to show the data on screen like transaction limits, available balance
    * */
    override fun fetchAllInitialApis() {
        callApiUseCase()
    }

    override fun callApiUseCase() {
        showLoading()
        bankTransferAPIUC.executeUseCase(
            BankTransferAPIUC.RequestValues(TransactionProductCode.TOPUP_VIA_EXTERNAL_CARD.pCode),
            object :
                UseCaseCallback<BankTransferAPIUC.ResponseValue, BankTransferAPIUC.ErrorValue> {
                override fun onSuccess(response: BankTransferAPIUC.ResponseValue) {
                    handleUseCaseResponse(response)
                    hideLoading()
                }

                override fun onError(error: BankTransferAPIUC.ErrorValue) {
                    _errorDescription.value = error.msg
                    hideLoading()
                }
            })
    }

    private fun handleUseCaseResponse(response: BankTransferAPIUC.ResponseValue) {
        viewState.transferFee.value = (response.transactionFeeResponse?.fixedAmount ?: 0.00).toString()
        viewState.minLimit?.value = response.fundTransferLimitsResponse?.minLimit.parseToDouble()
        viewState.maxLimit?.value = response.fundTransferLimitsResponse?.maxLimit.parseToDouble()
        transactionThreshold?.value = response.transactionThresholdResponse
        viewState.accountCurrentBalance?.value =
            response.accountBalanceResponse?.currentBalance.toString()
        reasonsList.value = response.reasonsResponse as ArrayList<FundTransferReasons?>
        viewState.isFocusable.value = true
    }

}