package com.yap.yappk.networking.microservices.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class BankTransferRequest(
    @SerializedName("beneficiaryId")
    val beneficiaryId: String? = null,
    @SerializedName("consumerId")
    val consumerId: String? = null,
    @SerializedName("amount")
    val amount: String? = null,
    @SerializedName("purposeCode")
    val purposeCode: String? = null,
    @SerializedName("purposeReason")
    val purposeReason: String? = null,
    @SerializedName("remarks")
    val remarks: String? = null,
    @SerializedName("feeAmount")
    val feeAmount: String? = null,
)