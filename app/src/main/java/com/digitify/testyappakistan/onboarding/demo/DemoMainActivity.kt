package com.digitify.testyappakistan.onboarding.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.extensions.hideSystemUI
import com.digitify.core.extensions.showSystemUI
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.digitify.testyappakistan.BR
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.DemoActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoMainActivity : BaseNavActivity<DemoActivityMainBinding, IMain.State, IMain.ViewModel>(),
    IMain.View {
    override fun getLayoutId(): Int = R.layout.demo_activity_main
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMain.ViewModel by viewModels<MainViewModel>()
    override val navigationGraphId: Int = R.navigation.nav_graph
    override val navigationGraphStartDestination: Int
        get() = intent?.getIntExtra(NAVIGATION_GRAPH_START_DESTINATION_ID, 0) ?: 0


    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        when (destination?.id) {
            R.id.accountSelectionFragment -> hideSystemUI(mViewBinding.root)
            else -> showSystemUI(mViewBinding.root)
        }
    }
}