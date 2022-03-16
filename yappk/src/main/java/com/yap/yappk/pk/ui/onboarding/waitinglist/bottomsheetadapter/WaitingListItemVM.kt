package com.yap.yappk.pk.ui.onboarding.waitinglist.bottomsheetadapter

import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.customers.responsedtos.InviteeDetails

class WaitingListItemVM(
    val inviteeDetail: InviteeDetails,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
)