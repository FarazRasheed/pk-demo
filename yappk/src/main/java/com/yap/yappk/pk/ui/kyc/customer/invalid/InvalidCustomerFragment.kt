package com.yap.yappk.pk.ui.kyc.customer.invalid

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentInvalidCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvalidCustomerFragment :
    BaseNavFragment<FragmentInvalidCustomerBinding, IInvalidCustomer.State, IInvalidCustomer.ViewModel>(
        R.layout.fragment_invalid_customer
    ), IInvalidCustomer.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IInvalidCustomer.ViewModel by viewModels<InvalidCustomerVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnLogout -> {
                //to be handle
            }
        }
    }
}