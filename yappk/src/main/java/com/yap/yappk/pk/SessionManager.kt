package com.yap.yappk.pk

import androidx.lifecycle.MutableLiveData
import com.digitify.core.enums.AccountType
import com.digitify.core.utils.*
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.CookiesManager
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.utils.toFormattedCurrency
import java.util.*

class SessionManager constructor(
    private val customersApi: CustomersApi,
    private val authApi: AuthApi,
    private val sharedPreferenceManager: SharedPreferenceManager
) {
    private var userAccounts: MutableLiveData<ArrayList<AccountInfo>> = MutableLiveData()
    val userAccount: MutableLiveData<AccountInfo> = MutableLiveData()
    var deepLinkFlowId: MutableLiveData<String?> = MutableLiveData(null)
    var accountBalance: MutableLiveData<String>? = MutableLiveData()

    suspend fun getAccountInfo(error: (message: String?) -> Unit = {}) {
        when (val response = customersApi.getAccountInfo()) {
            is ApiResponse.Success -> {
                userAccounts.value = response.data.data as ArrayList<AccountInfo>
                userAccount.value =
                    userAccounts.value?.firstOrNull { account -> account.accountType == AccountType.B2C_ACCOUNT.name }
                error.invoke(null)
            }
            is ApiResponse.Error -> {
                error.invoke(response.error.message)
                userAccounts.value = null
            }
        }
    }

    fun updateAccountData(accountInfo: AccountInfo) {
        userAccount.value = accountInfo
    }

    suspend fun getAccountBalance(error: (message: String?) -> Unit = {}) {
        when (val response = customersApi.getAccountBalance()) {
            is ApiResponse.Success -> {
                accountBalance?.value =
                    response.data.data?.currentBalance.toString().toFormattedCurrency()
                error.invoke(null)
            }
            is ApiResponse.Error -> {
                error.invoke(response.error.message)
                accountBalance?.value = null
            }
        }
    }

    fun expireUserSession() {
        val isFirstTimeUser: Boolean =
            sharedPreferenceManager.getValueBoolien(
                KEY_IS_FIRST_TIME_USER,
                false
            )
        var userName: String? = ""
        var countryCode: String? = ""
        val isRemember = sharedPreferenceManager.getValueBoolien(KEY_IS_REMEMBER, false)
        if (isRemember) {
            userName = sharedPreferenceManager.getDecryptedUserName()
            countryCode = sharedPreferenceManager.getDecryptedUserDialCode().toString()
        }

        sharedPreferenceManager.clearSharedPreference()
        CookiesManager.jwtToken = null
        CookiesManager.isLoggedIn = false
        sharedPreferenceManager.save(
            KEY_APP_UUID,
            UUID.randomUUID().toString()
        )

        if (isRemember) {
            sharedPreferenceManager.saveUserNameWithEncryption(userName ?: "")
            sharedPreferenceManager.saveUserDialCodeWithEncryption(countryCode ?: "").toString()
        }

        sharedPreferenceManager.save(KEY_IS_REMEMBER, isRemember)
        sharedPreferenceManager.save(
            KEY_IS_FIRST_TIME_USER,
            isFirstTimeUser
        )

        sharedPreferenceManager.save(
            KEY_IS_USER_LOGGED_IN,
            false
        )
        sharedPreferenceManager.save(
            KEY_TOUCH_ID_ENABLED,
            false
        )

        userAccounts.value = null
        userAccount.value = null
    }

    suspend fun doLogOut(accountUUID: String, error: (message: String?) -> Unit = {}) {
        when (val response = authApi.logout(accountUUID)) {
            is ApiResponse.Success -> {
                expireUserSession()
                error.invoke(null)
            }
            is ApiResponse.Error -> {
                error.invoke(response.error.message)
            }
        }
    }
}