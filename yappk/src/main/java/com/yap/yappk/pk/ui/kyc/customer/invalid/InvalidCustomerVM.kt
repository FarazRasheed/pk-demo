package com.yap.yappk.pk.ui.kyc.customer.invalid

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvalidCustomerVM @Inject constructor(
    override val viewState: InvalidCustomerState
) : BaseViewModel<IInvalidCustomer.State>(), IInvalidCustomer.ViewModel