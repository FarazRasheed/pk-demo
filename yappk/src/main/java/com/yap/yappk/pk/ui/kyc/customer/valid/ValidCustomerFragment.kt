package com.yap.yappk.pk.ui.kyc.customer.valid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.launchActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentValidCustomerBinding
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ValidCustomerFragment :
    BaseNavFragment<FragmentValidCustomerBinding, IValidCustomer.State, IValidCustomer.ViewModel>(
        R.layout.fragment_valid_customer
    ), IValidCustomer.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IValidCustomer.ViewModel by viewModels<ValidCustomerVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnGoToDashboard -> {
                launchActivity<PkDashboardActivity>(clearPrevious = true)
            }
        }
    }
}