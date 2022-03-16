package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail.updateprofileimage

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import java.io.File

interface IUpdateBankBeneficiaryImage {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val beneficiaryUpdated: LiveData<IBeneficiary?>
        val beneficiary: LiveData<IBeneficiary>
        fun setBeneficiary(beneficiary: IBeneficiary?)
        fun updateBeneficiary(beneficiaryId: String, file: File?)
    }

    interface State : IBase.State
}