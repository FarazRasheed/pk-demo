package com.yap.yappk.pk.ui.kyc.address.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.visible
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.uikit.widget.videoview.ExoPlayerCallBack
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardOrderSuccessBinding
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardOrderSuccessFragment :
    BaseNavFragment<FragmentCardOrderSuccessBinding, ICardOrderSuccess.State, ICardOrderSuccess.ViewModel>(
        R.layout.fragment_card_order_success
    ),
    ICardOrderSuccess.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ICardOrderSuccess.ViewModel by viewModels<CardOrderSuccessVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnGoToDashboard -> {
                launchActivity<PkDashboardActivity>(clearPrevious = true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
    }

    override fun onResume() {
        super.onResume()
        parentViewModel.setProgress(100)
        setCardAnimation()
    }

    private fun setCardAnimation() {
        mViewBinding.andExoPlayerView.setSource(R.raw.pk_card_on_its_way)
        mViewBinding.andExoPlayerView.player?.repeatMode = Player.REPEAT_MODE_OFF
        mViewBinding.andExoPlayerView.setExoPlayerCallBack(object : ExoPlayerCallBack {
            override fun onError() {
                mViewBinding.andExoPlayerView.setSource(R.raw.pk_card_on_its_way)
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onPositionDiscontinuity(reason: Int) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    mViewBinding.andExoPlayerView.visible()
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }
        })
    }
}