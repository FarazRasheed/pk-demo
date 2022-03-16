package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter

interface IMainBankTransfer {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val recentBeneficiariesList: LiveData<List<IBeneficiary>>
        val bankBeneficiariesList: LiveData<List<IBeneficiary>>
        var recentTransferAdapter: CoreRecentTransferAdapter
        var bankBeneficiaryAdapter: BankBeneficiaryListAdapter?
        val resourcesProvider: ResourcesProviders
        val isBeneficiaryDeleted: LiveData<Boolean>
        fun getBeneficiaries()
        fun removeBeneficiary(beneficiaryId: String)
    }

    interface State : IBase.State {
        val isNoRecentTransfers: MutableLiveData<Boolean>
        val isBeneficiariesAvailable: MutableLiveData<Boolean>
        val isRecentTransfersVisible: MutableLiveData<Boolean>
    }
}