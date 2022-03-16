package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankTransferSuccessVM @Inject constructor(
    override val viewState: BankTransferSuccessState
) :
    BaseViewModel<IBankTransferSuccess.State>(), IBankTransferSuccess.ViewModel {
    private val _beneficiary: MutableLiveData<BankTransferDataModel> = MutableLiveData()
    override val beneficiary: LiveData<BankTransferDataModel> = _beneficiary

    override fun setBeneficiary(beneficiary: BankTransferDataModel?) {
        _beneficiary.value = beneficiary
    }
}