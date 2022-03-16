package com.yap.yappk.pk.ui.onboarding.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.YapPkActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class YapPkMainActivity : BaseNavActivity<YapPkActivityMainBinding, IMain.State, IMain.ViewModel>(),
    IMain.View {
    override fun getLayoutId(): Int = R.layout.yap_pk_activity_main
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMain.ViewModel by viewModels<MainViewModel>()
    override val navigationGraphId: Int = R.navigation.pk_onboarding_nav_graph
    override val navigationGraphStartDestination: Int
        get() = intent?.getIntExtra(NAVIGATION_GRAPH_START_DESTINATION_ID, 0) ?: 0

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        //To use when needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val code = intent.getStringExtra("countryCode") ?: ""
            val mobile = intent.getStringExtra("mobileNo") ?: ""
            val startOnboardingTime = intent.getLongExtra("startOnboardingTime", 0)
            viewModel.signupData.countryCode = code
            viewModel.signupData.mobileNo = mobile.replace(" ", "")

            val date = Date()
            date.time = startOnboardingTime
            viewModel.viewState.startTime = date
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivBack -> {
                hideKeyboard()
                lifecycleScope.launch {
                    delay(100)
                    onBackPressed()
                }
            }
        }
    }
}