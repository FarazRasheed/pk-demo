package com.yap.yappk.pk.ui.onboarding.waitinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.utils.extensions.parseToInt
import com.yap.yappk.localization.screen_waiting_list_display_text_bottom_sheet_text_with_bumped_up_guide
import com.yap.yappk.localization.screen_waiting_list_display_text_bottom_sheet_text_with_bumped_up_spots_count
import com.yap.yappk.localization.screen_waiting_list_display_text_bottom_sheet_text_with_no_referred_users
import com.yap.yappk.localization.screen_waiting_list_display_text_bottom_sheet_text_with_referred_users_count
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.InviteeDetails
import com.yap.yappk.networking.microservices.customers.responsedtos.WaitingListResponse
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.onboarding.waitinglist.bottomsheetadapter.WaitingListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class WaitingListViewModel @Inject constructor(
    override val viewState: WaitingListState,
    private val customersApi: CustomersApi,
    private val resources: ResourcesProviders
    ) : BaseViewModel<IWaitingList.State>(), IWaitingList.ViewModel {
    private val _rankingResponse: MutableLiveData<WaitingListResponse> = MutableLiveData()
    override val rankingResponse: LiveData<WaitingListResponse> = _rankingResponse
    override var inviteeDetails: ArrayList<InviteeDetails> = arrayListOf()

    override fun getWaitingRankingList() {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.getWaitingRanking()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _rankingResponse.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _rankingResponse.value = null
                    }
                }
            }
        }
    }

    override fun inviteAFriend(request: SendInviteFriendRequest) {
        launch {
            customersApi.sendInviteFriend(request)
        }
    }

    override fun setDataValues(data: WaitingListResponse) {
        viewState.jump.set(data.jump ?: "0")
        viewState.rank.set(data.rank ?: "0")
        viewState.waitingBehind.set(data.waitingBehind ?: "0")
        viewState.signedUpUsers.set(
            (data.inviteeDetails?.size
                ?: 0).toString()
        )
        viewState.gainPoints.set(data.gainPoints ?: "0")
        viewState.totalGainedPoints.set(data.totalGainedPoints ?: "0")
        inviteeDetails = data.inviteeDetails ?: arrayListOf()
        setRankList(data.rank)
    }

    private fun setRankList(rank: String?) {
        rank?.let {
            val formattedRank = String.format("%07d", rank.parseToInt().absoluteValue) // 0009
            val digits = formattedRank.map { it.toString().parseToInt().absoluteValue }
            viewState.rankList = digits as MutableList<Int>
        }
    }

    override fun invitedFriendsAdapter(inviteeDetails: ArrayList<InviteeDetails>): WaitingListAdapter {
        val bottomSheetHeader = friendsListHeaderSection(
            title = invitedFriendHeadingBy(inviteeDetails.size),
            subTitle = invitedFriendContentBy(inviteeDetails.size)
        )
        val friends = arrayListOf(bottomSheetHeader)
        friends.addAll(inviteeDetails)
        return WaitingListAdapter(friends)
    }

    private fun invitedFriendContentBy(size: Int): String {
        return if (size == 0) resources.getString(
            screen_waiting_list_display_text_bottom_sheet_text_with_bumped_up_guide,
            viewState.jump.get() ?: "0"
        ) else resources.getString(
            screen_waiting_list_display_text_bottom_sheet_text_with_bumped_up_spots_count,
            viewState.totalGainedPoints.get() ?: "0"
        )
    }

    private fun invitedFriendHeadingBy(size: Int): String {
        return if (size == 0) resources.getString(
            screen_waiting_list_display_text_bottom_sheet_text_with_no_referred_users
        ) else resources.getString(
            screen_waiting_list_display_text_bottom_sheet_text_with_referred_users_count,
            viewState.signedUpUsers.get() ?: "0"
        )
    }

    private fun friendsListHeaderSection(title: String, subTitle: String): InviteeDetails {
        return InviteeDetails(
            title = title,
            subTitle = subTitle
        )
    }
}