package com.yap.yappk.pk.ui.kyc.enums

sealed class CardSchemeEnum(val scheme: String) {
    object MasterCard : CardSchemeEnum("Mastercard")
    object PayPak : CardSchemeEnum("PayPak")
}
