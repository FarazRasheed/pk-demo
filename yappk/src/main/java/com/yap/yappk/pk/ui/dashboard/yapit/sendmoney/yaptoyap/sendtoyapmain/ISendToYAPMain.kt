package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter

interface ISendToYAPMain {
    interface View : IBase.View<ViewModel> {
        var pagerSelectedPosition: Int
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val referralAmount: LiveData<ReferralAmount>
        val recentBeneficiariesList: LiveData<List<IBeneficiary>>
        val recentTransferAdapter: CoreRecentTransferAdapter
        val localContacts: LiveData<List<Contact>>
        val remoteContacts: LiveData<List<Contact>>
        val phoneContacts: LiveData<List<Contact>>
        val yapContacts: LiveData<List<Contact>>
        fun inviteAFriend(request: SendInviteFriendRequest)
        fun getTabTitle(position: Int): String
        fun shouldPostData(): Boolean
        fun setBeneficiaryList(beneficiaries: List<IBeneficiary>?)
        fun getReferralAmount()
        fun getY2YUsers(contacts: List<Contact>)
        fun setContacts(contacts: List<Contact>)
        fun getLocalContacts()
        fun getY2YUsersApiCall(contacts: List<Contact>)
    }

    interface State : IBase.State {
        val isNoRecentTransfers: MutableLiveData<Boolean>
        val isRecentTransfersVisible: MutableLiveData<Boolean>
    }
}