package com.digitify.testyappakistan.onboarding.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import com.digitify.core.base.BaseActivity
import com.digitify.core.extensions.hideSystemUI
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.NAVIGATION_GRAPH_ID
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.digitify.testyappakistan.BR
import com.digitify.testyappakistan.DeeplinkIntentHandler
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.ActivitySplashBinding
import com.digitify.testyappakistan.featureflag.SPFeatureFlagClient
import com.digitify.testyappakistan.onboarding.demo.DemoMainActivity
import com.yap.uikit.utils.animations.animators.ScaleAnimator
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.deeplinkNavigations.NotificationPayloadImpl
import com.yap.yappk.pk.ui.auth.main.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, ISplash.State, ISplash.ViewModel>(),
    ISplash.View {
    private var animatorSet: AnimatorSet? = null

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISplash.ViewModel by viewModels<SplashVM>()

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    @Inject
    lateinit var featureFlagProvider: SPFeatureFlagClient

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    @Inject
    lateinit var deeplinkIntentHandler: DeeplinkIntentHandler

    @Inject
    lateinit var notificationPayloadImpl: NotificationPayloadImpl

    init {
        animatorSet = AnimatorSet()
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI(mViewBinding.root)
        setRouteEventsObservers()
        playAnimationAndMoveNext()
    }

    private fun playAnimationAndMoveNext() {
        val scaleLogo =
            ScaleAnimator(
                1.0f,
                150.0f,
                AccelerateDecelerateInterpolator()
            ).with(mViewBinding.ivLogo, 1500)
        val scaleDot =
            ScaleAnimator(1.0f, 150.0f, AccelerateDecelerateInterpolator()).with(
                mViewBinding.ivDot,
                1500
            )
        scaleDot.startDelay = 400

        animatorSet?.play(scaleLogo)?.with(scaleDot)
        animatorSet?.interpolator = AccelerateDecelerateInterpolator()
        animatorSet?.start()
        animatorSet?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {// to be handle later
            }

            override fun onAnimationEnd(animation: Animator?) {
                viewModel.moveNext()
            }

            override fun onAnimationCancel(animation: Animator?) {
                // to be handle later
            }

            override fun onAnimationStart(animation: Animator?) {
                // to be handle later
            }

        })
    }

    private fun startDemoActivity(navigationEvent: SingleEvent<Boolean?>) {
        navigationEvent.getContentIfNotHandled()?.let {
            deeplinkIntentHandler.getDeeplinkIntentData(intent)?.let { flowId ->
                launchActivity<DemoMainActivity>(
                    options = Bundle(),
                    clearPrevious = true
                ) {
                    putExtra("flow_id", flowId)
                }
            }
        }
    }

    private fun startDemoActivityWithLogin(navigationEvent: SingleEvent<Boolean?>) {
        navigationEvent.getContentIfNotHandled()?.let {

            launchActivity<DemoMainActivity>(
                options = Bundle(),
                clearPrevious = true
            ) {
                putExtra(NAVIGATION_GRAPH_ID, R.navigation.nav_graph)
                putExtra(
                    NAVIGATION_GRAPH_START_DESTINATION_ID,
                    R.id.loginFragment
                )
                deeplinkIntentHandler.getDeeplinkIntentData(intent)?.let { flowId ->
                    putExtra("flow_id", flowId)
                }
            }
        }
    }


    private fun setRouteEventsObservers() {
        observeEvent(viewModel.openAuthentication, ::setRoute)
        observeEvent(viewModel.openDemo, ::startDemoActivity)
        observeEvent(viewModel.openDemoWithLogin, ::startDemoActivityWithLogin)

    }

    private fun startPkAuthenticationActivity(navigationEvent: SingleEvent<Boolean?>) {
        navigationEvent.getContentIfNotHandled()?.let {
            launchActivity<AuthenticationActivity>(clearPrevious = true) {
                putExtra(
                    "countryCode",
                    sharedPreferenceManager.getDecryptedUserDialCode().toString()
                )
                putExtra("mobileNo", sharedPreferenceManager.getDecryptedUserName())
            }
        }
    }

    // Route method from supper-app on Country code
    private fun setRoute(navigationEvent: SingleEvent<Boolean?>) {
        if (viewModel.viewState.selectedRegion.value == "PK") {
            notificationPayloadImpl.onReceivePayload(
                deeplinkIntentHandler.getDeeplinkIntentData(intent)
            )
            startPkAuthenticationActivity(navigationEvent)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        animatorSet?.cancel()
    }

}