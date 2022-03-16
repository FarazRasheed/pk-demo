package com.yap.yappk.pk.ui.onboarding.signupcompleted

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.getSpanableDynamicString
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.AnimationUtils
import com.digitify.core.utils.SingleEvent
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentSignupCompletedBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_onboarding_success_display_text_sub_title
import com.yap.yappk.pk.ui.kyc.main.KycDashboardActivity
import com.yap.yappk.pk.ui.onboarding.main.MainViewModel
import com.yap.yappk.pk.ui.onboarding.main.YapPkMainActivity
import com.yap.yappk.pk.utils.KYC_FROM_ONBOARDING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignupCompletedFragment :
    BaseNavFragment<FragmentSignupCompletedBinding, ISignupCompleted.State, ISignupCompleted.ViewModel>(
        R.layout.fragment_signup_completed
    ), ISignupCompleted.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISignupCompleted.ViewModel by viewModels<SignupCompletedVM>()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val windowSize: Rect = Rect() // to hold the size of the visible window

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.isWaiting.value = arguments?.getBoolean("IsWaiting", false)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
        initView()
    }

    private fun initView() {
        val display = activity?.windowManager?.defaultDisplay
        display?.getRectSize(windowSize)
        mViewBinding.rootContainer.children.forEach { it.alpha = 0f }
        val state = parentViewModel.viewState
        viewModel.startTime = state.startTime?.time ?: 0
        viewModel.elapsedOnboardingTime = TimeUnit.MILLISECONDS.toSeconds(
            Date().time - viewModel.startTime
        )
        viewModel.viewState.name.value =
            viewModel.sessionManager.userAccount.value?.currentCustomer?.firstName
        parentViewModel.setProgress(completeProgressValue)
        startToolBarAnimation()
    }

    private fun startToolBarAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(startAnimationDelay)
            toolbarAnimation().apply {
                addListener(onEnd = {
                    runAnimations()
                })
            }.start()
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> viewModel.navigate()
        }
    }

    private fun openWaitingListScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(
                destinationId,
                R.id.signupCompletedFragment
            )
        }
    }

    private fun openKycDashboardScreen(navigateEvent: SingleEvent<Any>) {
        navigateEvent.getContentIfNotHandled()?.let {
            launchActivity<KycDashboardActivity>(clearPrevious = true) {
                putExtra(KYC_FROM_ONBOARDING, true)
            }
        }
    }

    private fun toolbarAnimation(): AnimatorSet {
        val checkButton = (activity as YapPkMainActivity).findViewById<ImageView>(R.id.tbBtnCheck)
        val backButton = (activity as YapPkMainActivity).findViewById<ImageView>(R.id.ivBack)
        val progressbar =
            (activity as YapPkMainActivity).findViewById<LinearProgressIndicator>(R.id.progressBar)

        val checkBtnEndPosition = (windowSize.width() / 2) - (checkButton.width / 2)

        checkButton.isEnabled = true
        return AnimationUtils.runSequentially(
            AnimationUtils.pulse(checkButton),
            AnimationUtils.runTogether(
                AnimationUtils.fadeOut(backButton, parallelAnimationDelay),
                AnimationUtils.fadeOut(progressbar, parallelAnimationDelay)
            ),
            AnimationUtils.slideHorizontal(
                view = checkButton,
                from = checkButton.x,
                to = checkBtnEndPosition.toFloat(),
                duration = slideAnimationDelay
            )
        )
    }

    private fun runAnimations() {
        AnimationUtils.runSequentially(
            titleAnimation(),
            // Card Animation
            AnimationUtils.outOfTheBoxAnimation(mViewBinding.ivCard),
            // Bottom views animation
            AnimationUtils.runTogether(
                AnimationUtils.jumpInAnimation(mViewBinding.tvIbanTitle),
                AnimationUtils.jumpInAnimation(mViewBinding.tvIban).apply { startDelay = ibanAnimationDelay },
                AnimationUtils.jumpInAnimation(mViewBinding.tvNote)
                    .apply { startDelay = noteAnimationDelay },
                AnimationUtils.jumpInAnimation(mViewBinding.btnNext).apply { startDelay = buttonAnimationDelay }
            )
        ).apply {
            addListener(onEnd = {
            })
        }.start()
    }

    private fun titleAnimation(): AnimatorSet {
        val titleOriginalPosition = mViewBinding.tvHeading.y
        val subTitleOriginalPosition = mViewBinding.tvDescription.y
        val titleMidScreenPosition =
            (windowSize.height() / 2 - (mViewBinding.tvHeading.height)).toFloat()
        val subTitleMidScreenPosition = (windowSize.height() / 2 + 40).toFloat()

        // move to center position instantly without animation
        val moveToCenter = AnimationUtils.runTogether(
            AnimationUtils.slideVertical(
                mViewBinding.tvHeading,
                0,
                titleOriginalPosition,
                titleMidScreenPosition
            ),
            AnimationUtils.slideVertical(
                mViewBinding.tvDescription,
                0,
                subTitleOriginalPosition,
                subTitleMidScreenPosition
            )
        )

        // appear with alpha and scale animation
        val appearance = AnimationUtils.runTogether(
            AnimationUtils.outOfTheBoxAnimation(mViewBinding.tvHeading),
            AnimationUtils.outOfTheBoxAnimation(mViewBinding.tvDescription)
                .apply { startDelay = titleAnimationDelay }
        )

        val counter =
            counterAnimation(
                1,
                viewModel.elapsedOnboardingTime.toInt(),
                mViewBinding.tvDescription
            )
        val moveFromCenterToTop = AnimationUtils.runTogether(
            AnimationUtils.slideVertical(
                view = mViewBinding.tvHeading,
                from = titleMidScreenPosition,
                to = titleOriginalPosition,
                interpolator = AccelerateInterpolator()
            ),
            AnimationUtils.slideVertical(
                view = mViewBinding.tvDescription,
                from = subTitleMidScreenPosition,
                to = subTitleOriginalPosition,
                interpolator = AccelerateInterpolator()
            ).apply { startDelay = descriptionAnimationDelay }
        )

        val animationStack: ArrayList<Animator> = arrayListOf()
        animationStack.add(moveToCenter)
        animationStack.add(appearance)
        if (viewModel.elapsedOnboardingTime <= 60) animationStack.add(counter)
        animationStack.add(moveFromCenterToTop.apply { startDelay = completeTitleAnimationDelay })
        val array = arrayOfNulls<Animator>(animationStack.size)
        animationStack.toArray(array)
        return AnimationUtils.runSequentially(*array.requireNoNulls())
    }

    private fun counterAnimation(
        initialValue: Int,
        finalValue: Int,
        textview: TextView
    ): ValueAnimator {
        val text = requireContext().getString(screen_onboarding_success_display_text_sub_title)
        val parts = text.split("%1s")
        return AnimationUtils.valueCounter(initialValue, finalValue, counterAnimationDelay).apply {
            addUpdateListener { animator ->
                val counterText = animator.animatedValue.toString() + parts[1]
                val spannableString = (parts[0] + counterText).getSpanableDynamicString(
                    requireContext(),
                    counterText,
                    R.color.pkDarkSlateBlue
                )
                textview.text = spannableString
            }
        }
    }

    private fun viewModelObservers() {
        observeEvent(viewModel.openKycDashboard, ::openKycDashboardScreen)
        observeEvent(viewModel.openWaitingList, ::openWaitingListScreen)
    }
}
