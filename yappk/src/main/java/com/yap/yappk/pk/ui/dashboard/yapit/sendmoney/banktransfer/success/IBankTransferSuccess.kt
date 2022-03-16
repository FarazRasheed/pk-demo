package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.success

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.model.BankTransferDataModel

interface IBankTransferSuccess {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val beneficiary: LiveData<BankTransferDataModel>
        fun setBeneficiary(beneficiary: BankTransferDataModel?)
    }

    interface State : IBase.State
}
