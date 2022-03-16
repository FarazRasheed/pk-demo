package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.ui.dashboard.yapit.enum.ContactFragmentType
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts.PhoneContactsFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts.YapContactsFragment

class ContactsPagerAdapter(
    fragment: FragmentActivity,
    private var phoneContacts: List<Contact>,
    private var yapContacts: List<Contact>,
    private val referralAmount: ReferralAmount? = null,
    private val isSearch: Boolean? = false,
    private val callBack: (pos: Int) -> Unit
) : FragmentStateAdapter(fragment) {

    private val yapContactFragment: YapContactsFragment =
        YapContactsFragment(yapContacts, referralAmount, isSearch ?: false, callBack)
    private val phoneContactFragment: PhoneContactsFragment =
        PhoneContactsFragment(phoneContacts, referralAmount, isSearch ?: false, callBack)

    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ContactFragmentType.yapContact.type -> yapContactFragment
            ContactFragmentType.phoneContact.type -> phoneContactFragment
            else -> throw IllegalStateException("Pager position invalid: $position")
        }
    }

    fun searchContact(query: String) {
        if (yapContactFragment.isResumed)
            yapContactFragment.onSearchQuery(query)
        if (phoneContactFragment.isResumed)
            phoneContactFragment.onSearchQuery(query)
    }

    fun setPhoneContactList(phoneContacts: List<Contact>) {
        this.phoneContacts = phoneContacts
        phoneContactFragment.setContactList(phoneContacts)
    }

    fun setYapContactList(yapContacts: List<Contact>) {
        this.yapContacts = yapContacts
        yapContactFragment.setContactList(yapContacts)
    }
}