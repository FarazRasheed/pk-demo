package com.yap.yappk.pk.utils.enums

enum class TransactionProductCode(val pCode: String) {
    Y2Y_TRANSFER("P003"),
    ATM_WITHDRAWAL("P018"),
    POS_PURCHASE("P019"),
    E_COMMERCE("P036"),
    TOPUP_VIA_EXTERNAL_CARD("P009")
}