package com.yap.yappk.pk.ui.onboarding.waitinglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finishAffinity
import com.digitify.core.extensions.share
import com.digitify.core.utils.DateUtils
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.uikit.widget.videoview.ExoPlayerCallBack
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentWaitingListBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_invite_friend_display_text_share_url
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.InviteeDetails
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.onboarding.main.IMain
import com.yap.yappk.pk.ui.onboarding.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WaitingListFragment :
    BaseNavFragment<PkFragmentWaitingListBinding, IWaitingList.State, IWaitingList.ViewModel>(
        R.layout.pk_fragment_waiting_list
    ), IWaitingList.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IWaitingList.ViewModel by viewModels<WaitingListViewModel>()
    private val parentViewModel: IMain.ViewModel by activityViewModels<MainViewModel>()
    private var firstVideoPlayed: Boolean = false

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.getWaitingRankingList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setYapAnimation()
        setBackButtonDispatcher()
    }

    override fun onBackPressed(): Boolean {
        finishAffinity()
        return true
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvSignedUpUsers -> {
                getBottomSheetBuilder(viewModel.inviteeDetails)
                    ?.show(childFragmentManager, "")
            }
            R.id.btnShare -> {
                sendInviteApiCall()
                requireContext().share(
                    text = requireContext().getString(
                        screen_invite_friend_display_text_share_url,
                        pkBuildConfigurations.getAdjustURL(sessionManager.userAccount.value)
                    )
                )
            }
        }
    }

    private fun sendInviteApiCall() {
        val request = SendInviteFriendRequest(
            inviterCustomerId = sessionManager.userAccount.value?.currentCustomer?.customerId
                ?: "",
            referralDate = DateUtils.getCurrentDateWithFormat(DateUtils.SERVER_DATE_FULL_FORMAT)
        )
        viewModel.inviteAFriend(request)
    }

    private fun getBottomSheetBuilder(inviteeDetails: ArrayList<InviteeDetails>): BottomSheetUIDialog? {
        val builder =
            BottomSheetBuilder().setAdapter(viewModel.invitedFriendsAdapter(inviteeDetails) as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        return builder.build()
    }

    override fun setObservers() {
        viewModel.rankingResponse.observe(this, { waitingResponse ->
            waitingResponse?.let {
                viewModel.setDataValues(it)
                if (!viewModel.viewState.rankList.isNullOrEmpty())
                    runAnimation()
            }
        })
    }

    private fun runAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            mViewBinding.dtvRankOne.setValue(viewModel.viewState.rankList[0])
            mViewBinding.dtvRankTwo.setValue(viewModel.viewState.rankList[1])
                .apply { delay(100) }
            mViewBinding.dtvRankThree.setValue(viewModel.viewState.rankList[2])
                .apply { delay(150) }
            mViewBinding.dtvRankFour.setValue(viewModel.viewState.rankList[3])
                .apply { delay(200) }
            mViewBinding.dtvRankFive.setValue(viewModel.viewState.rankList[4])
                .apply { delay(250) }
            mViewBinding.dtvRankSix.setValue(viewModel.viewState.rankList[5])
                .apply { delay(300) }
            mViewBinding.dtvRankSeven.setValue(viewModel.viewState.rankList[6])
                .apply { delay(350) }
        }
    }

    private fun setYapAnimation() {
        mViewBinding.andExoPlayerView.setSource(R.raw.pk_waiting_list_first_part)
        mViewBinding.andExoPlayerView?.player?.repeatMode = Player.REPEAT_MODE_OFF
        mViewBinding.andExoPlayerView.setExoPlayerCallBack(object : ExoPlayerCallBack {
            override fun onError() {
                if (firstVideoPlayed)
                    mViewBinding.andExoPlayerView.setSource(
                        R.raw.pk_waiting_list_second_part
                    )
                else mViewBinding.andExoPlayerView.setSource(
                    R.raw.pk_waiting_list_first_part
                )
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                // later handle
            }

            override fun onPositionDiscontinuity(reason: Int) {}

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        mViewBinding.andExoPlayerView.visibility = View.VISIBLE
                    }

                    Player.STATE_ENDED -> {
                        if (!firstVideoPlayed) {
                            mViewBinding.andExoPlayerView.visibility = View.INVISIBLE
                            mViewBinding.andExoPlayerView.setSource(R.raw.pk_waiting_list_second_part)
                            mViewBinding.andExoPlayerView?.player?.repeatMode =
                                Player.REPEAT_MODE_ALL
                            firstVideoPlayed = true
                        }
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                // later handle
            }
        })
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.setProgressVisibility(false)
    }
}