package com.yap.yappk.pk.utils.enums

import androidx.annotation.Keep

@Keep
enum class PKAccountStatus {
    ON_BOARDED,
    SECRET_QUESTION_PENDING,
    SELFIE_PENDING,
    CARD_NAME_PENDING,
    ADDRESS_PENDING,
    CARD_SCHEME_PENDING,
    CARD_SCHEME_WITH_EXTERNAL_CARD_PENDING
}
