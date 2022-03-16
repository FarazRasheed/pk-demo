package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.ActivityMainQrBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainQrActivity :
    BaseNavActivity<ActivityMainQrBinding, IMainQr.State, IMainQr.ViewModel>(),
    IMainQr.View {
    override fun getLayoutId(): Int = R.layout.activity_main_qr
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMainQr.ViewModel by viewModels<MainQrVM>()
    override val navigationGraphId: Int = R.navigation.pk_qr_code_nav_graph
    override val navigationGraphStartDestination: Int
        get() = intent?.getIntExtra(NAVIGATION_GRAPH_START_DESTINATION_ID, 0) ?: 0

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) = Unit

}