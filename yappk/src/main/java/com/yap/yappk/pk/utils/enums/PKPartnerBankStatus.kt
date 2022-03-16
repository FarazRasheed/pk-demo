package com.yap.yappk.pk.utils.enums

import androidx.annotation.Keep

@Keep
enum class PKPartnerBankStatus {
    SIGN_UP_PENDING,
    ACTIVATED,
    IBAN_ASSIGNED,
    PHYSICAL_CARD_PENDING,
    PHYSICAL_CARD_SUCCESS
}
