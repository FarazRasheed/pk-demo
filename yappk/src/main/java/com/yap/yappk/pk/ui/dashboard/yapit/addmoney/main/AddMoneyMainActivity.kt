package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.ActivityAddMoneyMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMoneyMainActivity :
    BaseNavActivity<ActivityAddMoneyMainBinding, IAddMoneyMain.State, IAddMoneyMain.ViewModel>(),
    IAddMoneyMain.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_add_money_main
    override val viewModel: IAddMoneyMain.ViewModel by viewModels<AddMoneyMainVM>()
    override val navigationGraphId: Int = R.navigation.pk_add_money_nav_graph

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        // To be handle later
    }
}