package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model

import android.os.Parcelable
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.transactions.responsedtos.BankTransferResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferReasons
import kotlinx.parcelize.Parcelize


@Parcelize
data class BankTransferDataModel(
    val beneficiary: IBeneficiary? = null,
    var position: Int? = null,
    var transactionFees: String? = null,
    var amount: String? = null,
    var remarks: String? = null,
    var bankTransferResponse: BankTransferResponse? = null,
    var selectedReason: FundTransferReasons? = null
) :
    Parcelable