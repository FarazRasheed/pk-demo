package com.yap.yappk.pk.ui.auth

import com.digitify.core.utils.KEY_IS_FINGERPRINT_PERMISSION_SHOWN
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import javax.inject.Inject

class AccountRouteManager @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager) {

    fun getAccountRoute(account: AccountInfo?): AccountRoute {
        return account?.let {
            if (account.isWaiting)
                AccountRoute.WAITING
            else {
                if (account.iban.isNullOrBlank())
                    AccountRoute.ALLOWED
                else
                    if (!isBiometricSettingsShown())
                        AccountRoute.BIO_METRIC
                    else
                        if (account.otpBlocked == true) AccountRoute.OTP_BLOCKED else AccountRoute.DASHBOARD
            }
        } ?: return AccountRoute.NONE
    }

    private fun isBiometricSettingsShown(): Boolean {
        return sharedPreferenceManager.getValueBoolien(
            KEY_IS_FINGERPRINT_PERMISSION_SHOWN,
            false
        )
    }
}

enum class AccountRoute {
    WAITING,
    ALLOWED,
    BIO_METRIC,
    OTP_BLOCKED,
    DASHBOARD,
    NONE
}