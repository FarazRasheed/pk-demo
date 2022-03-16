package com.yap.yappk.pk.ui.auth.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity :
    BaseNavActivity<PkActivityAuthenticationBinding, IAuth.State, IAuth.ViewModel>(),
    IAuth.View {
    override fun getLayoutId(): Int = R.layout.pk_activity_authentication
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IAuth.ViewModel by viewModels<AuthViewModel>()
    override val navigationGraphId: Int = R.navigation.pk_signin_nav_graph

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        //To use when needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mobileNo = intent.getStringExtra("mobileNo") ?: ""
        viewModel.countryCode = intent.getStringExtra("countryCode") ?: ""
        viewModel.viewState.isAccountBlocked = intent.getBooleanExtra("isAccountBlocked", false)
    }

    override fun onClick(id: Int) = Unit
}