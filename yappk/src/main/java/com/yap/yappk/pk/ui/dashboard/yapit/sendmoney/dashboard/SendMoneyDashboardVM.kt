package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.dashboard

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendMoneyDashboardVM @Inject constructor(
    override val viewState: SendMoneyDashboardState,
    private val customersApi: CustomersApi
) : BaseViewModel<ISendMoneyDashboard.State>(), ISendMoneyDashboard.ViewModel {
    override var recentTransferAdapter: CoreRecentTransferAdapter = CoreRecentTransferAdapter(
        mutableListOf()
    )
}