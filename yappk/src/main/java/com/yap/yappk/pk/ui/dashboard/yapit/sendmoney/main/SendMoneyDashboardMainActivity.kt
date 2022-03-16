package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkActivitySendMoneyDashboardMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendMoneyDashboardMainActivity :
    BaseNavActivity<PkActivitySendMoneyDashboardMainBinding, ISendMoneyDashboardMain.State, ISendMoneyDashboardMain.ViewModel>() {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.pk_activity_send_money_dashboard_main
    override val viewModel: ISendMoneyDashboardMain.ViewModel by viewModels<SendMoneyDashboardMainVM>()

    override val navigationGraphId: Int = R.navigation.pk_send_money_navigation

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        // To be handle later
    }

}