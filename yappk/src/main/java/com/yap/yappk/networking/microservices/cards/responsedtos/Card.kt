package com.yap.yappk.networking.microservices.cards.responsedtos


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    @SerializedName("accountType")
    val accountType: String? = null,
    @SerializedName("accountUuid")
    val accountUuid: String? = null,
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("atmAllowed")
    var atmAllowed: Boolean? = null,
    @SerializedName("availableBalance")
    val availableBalance: Double? = null,
    @SerializedName("bioMetricRequired")
    val bioMetricRequired: Boolean? = null,
    @SerializedName("blocked")
    val blocked: Boolean? = null,
    @SerializedName("cardBalance")
    val cardBalance: Double? = null,
    @SerializedName("cardName")
    var cardName: String? = null,
    @SerializedName("cardScheme")
    val cardScheme: String? = null,
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String? = null,
    @SerializedName("cardType")
    var cardType: String? = null,
    @SerializedName("currentBalance")
    val currentBalance: Double? = null,
    @SerializedName("customerId")
    val customerId: String? = null,
    @SerializedName("delivered")
    val delivered: Boolean? = null,
    @SerializedName("expiryDate")
    val expiryDate: String? = null,
    @SerializedName("maskedCardNo")
    val maskedCardNo: String? = null,
    @SerializedName("nameUpdated")
    var nameUpdated: Boolean? = null,
    @SerializedName("onlineBankingAllowed")
    val onlineBankingAllowed: Boolean? = null,
    @SerializedName("paymentAbroadAllowed")
    val paymentAbroadAllowed: Boolean? = null,
    @SerializedName("physical")
    val physical: Boolean? = null,
    @SerializedName("physicalCardOrdered")
    val physicalCardOrdered: Boolean? = null,
    @SerializedName("pinCreated")
    var pinCreated: Boolean? = null,
    @SerializedName("pinStatus")
    var pinStatus: String? = null,
    @SerializedName("frontImage")
    val frontImage: String? = null,
    @SerializedName("backImage")
    val backImage: String? = null,
    @SerializedName("productCode")
    val productCode: String? = null,
    @SerializedName("retailPaymentAllowed")
    var retailPaymentAllowed: Boolean? = null,
    @SerializedName("shipmentStatus")
    val shipmentStatus: String? = null,
    @SerializedName("shipmentStatusId")
    val shipmentStatusId: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("deliveryStatus")
    var deliveryStatus: String? = null,
    @Transient
    var type: Int? = 0
) : Parcelable