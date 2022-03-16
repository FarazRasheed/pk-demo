package com.yap.yappk.pk.ui.onboarding.topqueue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.yap.uikit.widget.videoview.ExoPlayerCallBack
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentReachedQueueTopBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.ui.kyc.main.KycDashboardActivity
import com.yap.yappk.pk.utils.KYC_FROM_ONBOARDING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ReachedQueueTopFragment :
    BaseNavFragment<FragmentReachedQueueTopBinding, IReachedQueueTop.State, IReachedQueueTop.ViewModel>(
        R.layout.fragment_reached_queue_top
    ), IReachedQueueTop.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IReachedQueueTop.ViewModel by viewModels<ReachedQueueTopVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCompleteVerificationObserver()
        observeEvents()
        setBackButtonDispatcher()
    }

    private fun setCompleteVerificationObserver() {
        viewModel.isCompleteVerification.observe(this, Observer { isCompleteVerification ->
            if (isCompleteVerification) viewModel.openCompleteVerificationScreen()
        })
    }

    private fun openKycDashboardScreen(navigationEvent: SingleEvent<AccountInfo?>) {
        navigationEvent.getContentIfNotHandled()?.let {
            launchActivity<KycDashboardActivity>(clearPrevious = true) {
                putExtra(KYC_FROM_ONBOARDING, true)
            }
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnCompleteVerification -> {
                viewModel.completeVerification(viewModel.getCompleteVerificationRequest())
            }
        }
    }

    private fun observeEvents() {
        observeEvent(viewModel.openCompleteVerification, ::openKycDashboardScreen)

    }

    override fun onResume() {
        super.onResume()
        launch(Dispatcher.Main) {
            delay(200)
            setCardAnimation()
        }
    }

    override fun setCardAnimation() {
        mViewBinding.andExoPlayerView.setSource(R.raw.pk_yap_card_animation)
        mViewBinding.andExoPlayerView?.player?.repeatMode = Player.REPEAT_MODE_OFF
        mViewBinding.andExoPlayerView.setExoPlayerCallBack(object : ExoPlayerCallBack {
            override fun onError() {
                mViewBinding.andExoPlayerView.setSource(R.raw.pk_yap_card_animation)
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                //To be handle later
            }

            override fun onPositionDiscontinuity(reason: Int) {
                //To be handle later
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    mViewBinding.andExoPlayerView.visibility = View.VISIBLE
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                //To be handle later

            }
        })
    }

}