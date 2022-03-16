package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemittanceFee(
    @SerializedName("feeType")
    val feeType: String? = null,
    @SerializedName("fixedAmount")
    val fixedAmount: Double? = 0.0,
    @SerializedName("vat")
    val vat: Double? = 0.0,
    @SerializedName("totalFee")
    val totalFee: Double? = 0.0,
    @SerializedName("displayOnly")
    val displayOnly: Boolean? = false,
    @SerializedName("tierRateDTOList")
    val tierRateDTOList: List<TierRateDTO>? = arrayListOf()
) : Parcelable {
    @Parcelize
    data class TierRateDTO(
        @SerializedName("amountFrom")
        val amountFrom: Double? = 0.0,
        @SerializedName("amountTo")
        val amountTo: Double? = 0.0,
        @SerializedName("feeAmount")
        var feeAmount: Double? = 0.0,
        @SerializedName("vatAmount")
        var vatAmount: Double? = 0.0,
        @SerializedName("feePercentage")
        var feePercentage: String? = null,
        @SerializedName("vatPercentage")
        var vatPercentage: String? = null,
        @SerializedName("feeInPercentage")
        var feeInPercentage: Boolean? = null
    ) : Parcelable
}
