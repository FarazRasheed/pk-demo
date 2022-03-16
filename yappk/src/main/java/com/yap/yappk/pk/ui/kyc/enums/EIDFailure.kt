package com.yap.yappk.pk.ui.kyc.enums


/**
Created by Faheem Riaz on 07/07/2021.
 **/

enum class EIDFailure(val errorCode: String) {
    EXPIRED_EID_REJECTED("1064"),
    UNDER_AGE_EID_REJECTED("1081")
}