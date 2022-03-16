package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMoneyDashboardVM @Inject constructor(
    override val viewState: AddMoneyDashboardState
) : BaseViewModel<IAddMoneyDashboard.State>(), IAddMoneyDashboard.ViewModel