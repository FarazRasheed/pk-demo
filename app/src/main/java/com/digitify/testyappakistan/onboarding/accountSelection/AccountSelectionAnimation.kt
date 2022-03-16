package com.digitify.testyappakistan.onboarding.accountSelection

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.digitify.testyappakistan.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.material.textview.MaterialTextView
import com.yap.uikit.widget.videoview.AndExoPlayerView
import com.yap.uikit.widget.videoview.ExoPlayerCallBack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountSelectionAnimation @Inject constructor(){
    var captionsIndex: Int = 0
    val handler = Handler()
    private var animatorSet: AnimatorSet? = null
    var isPaused = false

    fun getCaptionsList(): List<String> {
        return listOf(
            "Bank your way", "Get an account in seconds", "Money transfers made simple",
            "Track your spending", "Split bills effortlessly", "Spend locally wherever you go",
            "Instant spending notifications", "An app for everyone"
        )
    }

    fun getDelaysList(): List<Int> {
        return listOf(
            1800, 1000, 1800, 1800, 2500, 1800, 2800, 3000
        )
    }

    fun setupPlayer(andExoPlayerView: AndExoPlayerView, tvCaption: MaterialTextView) {
        andExoPlayerView.setSource(R.raw.yap_demo_intro)
        captionsIndex = 0
        handler.postDelayed(runnable, 1000)
        andExoPlayerView.setExoPlayerCallBack(object : ExoPlayerCallBack {
            override fun onError() {
                handler.removeCallbacks(runnable)
                andExoPlayerView.setSource(R.raw.demo_test)
                captionsIndex = 0
                handler.postDelayed(runnable, 1000)
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {}

            override fun onPositionDiscontinuity(reason: Int) {
                animatorSet?.cancel()
                animatorSet = null
                //captionsIndex = 0
                tvCaption?.postDelayed({
                    captionsIndex = 0
                    playCaptionAnimation(tvCaption)
                }, 1800)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    captionsIndex = 0
                    tvCaption.postDelayed({
                        playCaptionAnimation(tvCaption)
                    }, 1800)
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
        })
    }

    private val runnable = Runnable {
//        mViewBinding.layoutButtons.let {
////            YoYo.with(Techniques.FadeIn).duration(1500)
////                .onStart { mViewBinding.layoutButtons.visibility = View.VISIBLE }.playOn(mViewBinding.layoutButtons)
//        }
    }

    fun playCaptionAnimation(tvCaption: MaterialTextView) {
        var animatorSet: AnimatorSet? = null
        tvCaption.let {
            if (!isPaused && captionsIndex != -1) {
                tvCaption.text = getCaptionsList()[captionsIndex]
                val fadeIn = ObjectAnimator.ofFloat(
                    it,
                    View.ALPHA,
                    0f, 1f
                )
                fadeIn.interpolator = DecelerateInterpolator() //add this
                fadeIn.duration = 400
                val fadeOut = ObjectAnimator.ofFloat(
                    it,
                    View.ALPHA,
                    1f, 0f
                )
                fadeOut.interpolator = AccelerateInterpolator() //add this
                fadeOut.duration = 400
                fadeOut.startDelay = getDelaysList()[captionsIndex].toLong()
                animatorSet = AnimatorSet()
                animatorSet?.interpolator = AccelerateDecelerateInterpolator()
                animatorSet?.playSequentially(fadeIn, fadeOut)
                animatorSet?.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        captionsIndex += 1
                        if (captionsIndex < getCaptionsList().size) {
                            playCaptionAnimation(tvCaption)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        captionsIndex = 0
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        tvCaption.visibility = View.VISIBLE
                    }
                })
                animatorSet?.start()
            }
        }
    }
}