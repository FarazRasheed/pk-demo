package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionfilter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.transactions.responsedtos.FilterAmount
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import com.yap.yappk.pk.utils.rangebar.OnRangeChangedListener

interface ICardTransactionFilter {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
        fun getFragmentArguments()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val sessionManager: SessionManager
        val filterAmount: LiveData<FilterAmount>
        var filterData: FilterExtras?
        fun getRangeTitle(): String
        fun fetchFilterAmount()
        fun setFilterData()
        fun getChangeListener(): OnRangeChangedListener
    }

    interface State : IBase.State {
        val isAtmWithdrawAllow: MutableLiveData<Boolean>
        val isRetailPayment: MutableLiveData<Boolean>
        val minRange: MutableLiveData<String>
        val maxRange: MutableLiveData<String>
    }
}