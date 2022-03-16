package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.share
import com.digitify.core.utils.DateUtils
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentYapContactsBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_invite_friend_display_text_share_url
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts.adapter.YapContactListAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search.OnSearchListener
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main.YapToYapTransferFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YapContactsFragment(
    private val yapContacts: List<Contact>,
    private val referralAmount: ReferralAmount?,
    private val isSearchView: Boolean,
    private val callBack: (pos: Int) -> Unit
) :
    BaseNavFragment<FragmentYapContactsBinding, IYapContacts.State, IYapContacts.ViewModel>(
        R.layout.fragment_yap_contacts
    ),
    IYapContacts.View, OnSearchListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IYapContacts.ViewModel by viewModels<YapContactsVM>()

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
        viewModel.yapContactsListAdapter =
            YapContactListAdapter(
                list = yapContacts.toMutableList(),
                resourcesProviders = viewModel.resourcesProviders,
                referralAmount = referralAmount,
                isSearchView = isSearchView
            )
        viewModel.yapContactsListAdapter?.onItemClickListener = rvItemClickListener
        mViewBinding.rvYapContacts.adapter = viewModel.yapContactsListAdapter
        if (isSearchView) viewModel.setContactList(yapContacts)
    }


    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is String -> {
                    if (data.isNullOrEmpty())
                        inviteFriend()
                }
                is Contact -> openY2YTransferScreen(contact = data)
            }
        }
    }

    private fun openY2YTransferScreen(contact: Contact) {
        navigate(
            if (isSearchView) R.id.action_contactSearchFragment_to_pk_y2y_transfer_nav
            else R.id.action_sendToYAPMainFragment_to_pk_y2y_transfer_nav,
            bundleOf(YapToYapTransferFragment::class.java.name to contact)
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
        viewModel.yapContactsListAdapter?.filter?.filter(query)
    }

    fun setContactList(yapContacts: List<Contact>) {
        viewModel.setContactList(yapContacts)
    }

    override fun viewModelObservers() {
        observe(viewModel.searchQuery, ::handleSearchQuery)

    }
}
