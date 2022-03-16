package com.yap.yappk.pk.ui.kyc.enums

import androidx.annotation.Keep

@Keep
enum class DocScanStatus {
    SCAN_PENDING,
    SCAN_COMPLETED,
    DOCS_UPLOADED,
    EXPIRED_EID_REJECTED,
    UNDER_AGE_EID_REJECTED,
    NONE
}