package com.yap.yappk.pk.ui.kyc.customer.valid

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ValidCustomerVM @Inject constructor(
    override val viewState: ValidCustomerState
) : BaseViewModel<IValidCustomer.State>(), IValidCustomer.ViewModel