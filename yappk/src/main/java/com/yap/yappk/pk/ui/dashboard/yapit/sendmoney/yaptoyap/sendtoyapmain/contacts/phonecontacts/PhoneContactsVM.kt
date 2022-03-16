package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts.adapter.PhoneContactListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhoneContactsVM @Inject constructor(
    override val viewState: PhoneContactsState,
    private val customersApi: CustomersApi,
    override val resourcesProviders: ResourcesProviders
) : BaseViewModel<IPhoneContacts.State>(), IPhoneContacts.ViewModel {
    override var phoneContactsListAdapter: PhoneContactListAdapter? = null
    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    override val searchQuery: LiveData<String> = _searchQuery

    override fun setContactList(phoneContacts: List<Contact>) {
        phoneContactsListAdapter?.updateContactListItems(phoneContacts)
    }

    override fun inviteAFriend(request: SendInviteFriendRequest) {
        launch {
            customersApi.sendInviteFriend(request)
        }
    }

    override fun onSearchQuery(query: String) {
        _searchQuery.value = query
    }

}