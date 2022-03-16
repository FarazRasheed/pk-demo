package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.dashboard

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter

interface ISendMoneyDashboard {
    interface View : IBase.View<ViewModel> {
        fun openAccountDetailScreen()
        fun openYapToYapScreen()
        fun setViewModelObservers()
        fun openSearchScreen()
        fun initListeners()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var recentTransferAdapter: CoreRecentTransferAdapter
    }

    interface State : IBase.State {
        var isNoRecentTransfers: MutableLiveData<Boolean>
        var isRecentTransfersVisible: MutableLiveData<Boolean>
    }
}