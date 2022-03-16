package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary

interface ISendMoneyDashboardMain {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val recentBeneficiariesList: LiveData<List<IBeneficiary>>
        val phoneContacts: LiveData<List<Contact>>
        val yapContacts: LiveData<List<Contact>>
        fun setYapContact(yapContact: List<Contact>)
        fun setPhoneContact(phoneContact: List<Contact>)
        fun getRecentTransferBeneficiaries()
    }

    interface State : IBase.State
}