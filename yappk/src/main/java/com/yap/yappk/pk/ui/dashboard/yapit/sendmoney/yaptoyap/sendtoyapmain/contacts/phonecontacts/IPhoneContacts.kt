package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts

import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts.adapter.PhoneContactListAdapter

interface IPhoneContacts {
    interface View : IBase.View<ViewModel> {
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var phoneContactsListAdapter: PhoneContactListAdapter?
        val resourcesProviders: ResourcesProviders
        val searchQuery: LiveData<String>
        fun onSearchQuery(query: String)
        fun inviteAFriend(request: SendInviteFriendRequest)
        fun setContactList(phoneContacts: List<Contact>)

    }

    interface State : IBase.State
}