package com.yap.yappk.networking.microservices.transactions.requestdtos

import com.google.gson.annotations.SerializedName

data class Y2YFundsTransferRequest(
    @SerializedName("receiverUUID") var receiverUUID: String?,
    @SerializedName("beneficiaryName") var beneficiaryName: String?,
    @SerializedName("deviceId") var deviceId: String?,
    @SerializedName("amount") var amount: String?,
    @SerializedName("otpVerificationReq") var otpVerificationReq: Boolean?,
    @SerializedName("remarks") var remarks: String?
)