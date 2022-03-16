package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.phonecontacts.adapter

import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact

class PhoneContactItemViewModel(
    val contact: Contact,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
)