package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountdetails

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.localization.screen_account_detail_display_text_account_number
import com.yap.yappk.localization.screen_account_detail_display_text_account_title
import com.yap.yappk.localization.screen_account_detail_display_text_iban
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountDetailVM @Inject constructor(
    override val viewState: AccountDetailState,
    override val sessionManager: SessionManager,
    private val resourcesProvider: ResourcesProviders,

    ) : BaseViewModel<IAccountDetail.State>(), IAccountDetail.ViewModel {
    override fun onCreate() {
        super.onCreate()
        viewState.accountNumber.value = sessionManager.userAccount.value?.accountNo
        viewState.accountTitle.value =
            sessionManager.userAccount.value?.currentCustomer?.firstName + " " + sessionManager.userAccount.value?.currentCustomer?.lastName
        viewState.iban.value = sessionManager.userAccount.value?.iban
    }

    override fun getShareBody(): String {
        return "${resourcesProvider.getString(screen_account_detail_display_text_account_title)}: ${viewState.accountTitle.value}\n" +
                "${resourcesProvider.getString(screen_account_detail_display_text_iban)}: ${viewState.iban.value}\n" +
                "${resourcesProvider.getString(screen_account_detail_display_text_account_number)}: ${viewState.accountNumber.value}\n"
    }
}