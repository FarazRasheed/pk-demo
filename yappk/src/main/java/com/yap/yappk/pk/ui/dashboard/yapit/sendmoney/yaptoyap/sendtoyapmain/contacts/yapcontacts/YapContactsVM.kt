package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts.adapter.YapContactListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YapContactsVM @Inject constructor(
    override val viewState: YapContactsState,
    private val customersApi: CustomersApi,
    override val resourcesProviders: ResourcesProviders
) : BaseViewModel<IYapContacts.State>(), IYapContacts.ViewModel {
    override var yapContactsListAdapter: YapContactListAdapter? = null
    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    override val searchQuery: LiveData<String> = _searchQuery


    override fun inviteAFriend(request: SendInviteFriendRequest) {
        launch {
            customersApi.sendInviteFriend(request)
        }
    }

    override fun onSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun setContactList(yapContacts: List<Contact>) {
        yapContactsListAdapter?.updateContactListItems(yapContacts)
    }

}