package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.share
import com.digitify.core.utils.DateUtils
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentPhoneContactsBinding
import com.yap.yappk.localization.common_display_text_y2y_share
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_invite_friend_display_text_share_url
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts.adapter.PhoneContactListAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search.OnSearchListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhoneContactsFragment(
    private val phoneContacts: List<Contact>,
    private val referralAmount: ReferralAmount?,
    private val isSearchView: Boolean,
    private val callBack: (pos: Int) -> Unit
) : BaseNavFragment<FragmentPhoneContactsBinding, IPhoneContacts.State, IPhoneContacts.ViewModel>(R.layout.fragment_phone_contacts),
    IPhoneContacts.View, OnSearchListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IPhoneContacts.ViewModel by viewModels<PhoneContactsVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModelObservers()
    }

    private fun setUpRecyclerView() {
        viewModel.phoneContactsListAdapter =
            PhoneContactListAdapter(
                list = phoneContacts.toMutableList(),
                resourcesProviders = viewModel.resourcesProviders,
                referralAmount = referralAmount,
                isSearchView = isSearchView
            )
        viewModel.phoneContactsListAdapter?.onItemClickListener = rvItemClickListener
        mViewBinding.rvPhoneContacts.adapter = viewModel.phoneContactsListAdapter
        if (isSearchView) viewModel.setContactList(phoneContacts)
    }


    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is String -> {
                    if (data.isNullOrEmpty())
                        inviteFriend()
                }
                is Contact -> inviteContactFriend(data)
            }
        }
    }

    private fun inviteContactFriend(data: Contact) {
        sendInviteApiCall()
        requireContext().share(
            text = requireContext().getString(
                common_display_text_y2y_share,
                data.title?.split(" ")?.first() ?: "",
                sessionManager.userAccount.value?.currentCustomer?.firstName ?: "",
                pkBuildConfigurations.getAdjustURL(sessionManager.userAccount.value)
            )
        )
    }

    private fun inviteFriend() {
        sendInviteApiCall()
        requireContext().share(
            text = requireContext().getString(
                screen_invite_friend_display_text_share_url,
                pkBuildConfigurations.getAdjustURL(sessionManager.userAccount.value)
            )
        )
    }

    private fun sendInviteApiCall() {
        val request = SendInviteFriendRequest(
            inviterCustomerId = sessionManager.userAccount.value?.currentCustomer?.customerId
                ?: "",
            referralDate = DateUtils.getCurrentDateWithFormat(DateUtils.SERVER_DATE_FULL_FORMAT)
        )
        viewModel.inviteAFriend(request)
    }

    override fun onClick(id: Int) {
        when (id) {
            // handle later
        }
    }

    override fun onSearchQuery(query: String) {
        viewModel.onSearchQuery(query)
    }

    private fun handleSearchQuery(query: String) {
        viewModel.phoneContactsListAdapter?.filter?.filter(query)
    }


    fun setContactList(phoneContacts: List<Contact>) {
        viewModel.setContactList(phoneContacts)
    }

    override fun viewModelObservers() {
        observe(viewModel.searchQuery, ::handleSearchQuery)
    }
}