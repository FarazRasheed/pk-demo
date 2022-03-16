package com.yap.yappk.pk.ui.kyc

import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.utils.enums.PKAccountStatus

class KYCRouteManager {

    fun getKycScreenRoute(account: AccountInfo?): KycRoute {
        return account?.let {
            when (it.notificationStatuses) {
                PKAccountStatus.ON_BOARDED.name -> {
                    KycRoute.ON_BOARDED
                }
                PKAccountStatus.SECRET_QUESTION_PENDING.name -> {
                    KycRoute.SECRET_QUESTION_SCREEN
                }
                PKAccountStatus.SELFIE_PENDING.name -> {
                    KycRoute.SELFIE_SCREEN
                }
                PKAccountStatus.CARD_SCHEME_PENDING.name,
                PKAccountStatus.CARD_NAME_PENDING.name,
                PKAccountStatus.ADDRESS_PENDING.name,
                PKAccountStatus.CARD_SCHEME_WITH_EXTERNAL_CARD_PENDING.name -> {
                    KycRoute.CARD_SCHEME_SCREEN
                }
                else -> KycRoute.NONE
            }
        } ?: return KycRoute.NONE
    }
}

enum class KycRoute {
    SECRET_QUESTION_SCREEN,
    SELFIE_SCREEN,
    CARD_SCHEME_SCREEN,
    ON_BOARDED,
    NONE
}
