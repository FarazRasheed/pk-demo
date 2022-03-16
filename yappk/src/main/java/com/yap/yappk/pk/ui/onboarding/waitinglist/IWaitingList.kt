package com.yap.yappk.pk.ui.onboarding.waitinglist

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.InviteeDetails
import com.yap.yappk.networking.microservices.customers.responsedtos.WaitingListResponse
import com.yap.yappk.pk.ui.onboarding.waitinglist.bottomsheetadapter.WaitingListAdapter

interface IWaitingList {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val rankingResponse: LiveData<WaitingListResponse>
        var inviteeDetails: ArrayList<InviteeDetails>
        fun getWaitingRankingList()
        fun setDataValues(data: WaitingListResponse)
        fun invitedFriendsAdapter(inviteeDetails: ArrayList<InviteeDetails>): WaitingListAdapter
        fun inviteAFriend(request: SendInviteFriendRequest)
    }

    interface State : IBase.State {
        var jump: ObservableField<String>
        var rank: ObservableField<String>
        var signedUpUsers: ObservableField<String>
        var waitingBehind: ObservableField<String>
        var gainPoints: ObservableField<String>
        var rankList: MutableList<Int>
        var totalGainedPoints: ObservableField<String>

    }
}