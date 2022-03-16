package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.responsedtos.AccountBalance
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferLimits
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionFeeResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.TransactionThresholdResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.YapToYapTransaction
import com.yap.yappk.pk.SessionManager

interface IYapToYapTransfer {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun getCombinedApisParallel()
        fun fetchAllApis(responses: (ApiResponse<BaseResponse<TransactionFeeResponse>>?, ApiResponse<BaseResponse<FundTransferLimits>>?, ApiResponse<BaseResponse<TransactionThresholdResponse>>?, ApiResponse<BaseResponse<AccountBalance>>) -> Unit)
        fun onAmountChange(amount: CharSequence, start: Int, before: Int, count: Int)
        fun y2yTransferRequest(
            receiverUUID: String?,
            beneficiaryName: String?,
            deviceId: String?,
            amount: String?,
            otpVerificationReq: Boolean?,
            remarks: String?
        )

        var transactionThreshold: MutableLiveData<TransactionThresholdResponse>?
        val sessionManager: SessionManager
        val y2yTransferSuccess: LiveData<YapToYapTransaction>
        val errorDescription: LiveData<CharSequence>
        val sharedPreferenceManager: SharedPreferenceManager
    }

    interface State : IBase.State {
        var contact: MutableLiveData<IBeneficiary>
        var isValidAmount: MutableLiveData<Boolean>
        var isFocusable: MutableLiveData<Boolean>
        var transferFee: MutableLiveData<String>
        var minLimit: MutableLiveData<Double>?
        var maxLimit: MutableLiveData<Double>?
        var accountCurrentBalance: MutableLiveData<String>?
    }
}
