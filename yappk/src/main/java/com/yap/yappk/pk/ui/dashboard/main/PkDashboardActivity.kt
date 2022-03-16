package com.yap.yappk.pk.ui.dashboard.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.digitify.core.base.BaseActivity
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.visible
import com.google.android.material.navigation.NavigationBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.DashboardActivityBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.featureflag.KEY_FLAG_YP_PK_ADD_MONEY
import com.yap.yappk.pk.featureflag.KEY_FLAG_YP_PK_PAY_BILLS
import com.yap.yappk.pk.featureflag.KEY_FLAG_YP_PK_SEND_MONEY
import com.yap.yappk.pk.featureflag.PKFeatureFlagClient
import com.yap.yappk.pk.ui.dashboard.main.pageradapter.DashboardPagerAdaptor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main.AddMoneyMainActivity
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.SendMoneyDashboardMainActivity
import com.yap.yappk.pk.utils.startAnimation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PkDashboardActivity :
    BaseActivity<DashboardActivityBinding, IDashboard.State, IDashboard.ViewModel>(),
    IDashboard.View {
    override fun getLayoutId(): Int = R.layout.dashboard_activity
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IDashboard.ViewModel by viewModels<DashboardVM>()
    private lateinit var toBottom: Animation
    private lateinit var rotateClose: Animation
    private lateinit var rotateOpen: Animation
    private lateinit var fromBottom: Animation
    private lateinit var explosionOpen: Animation
    private lateinit var explosionClose: Animation

    private val animDuration: Long = 500

    @Inject
    lateinit var pkFeatureFlags: PKFeatureFlagClient

    override fun onClick(id: Int) {
        when (id) {
            R.id.fabYapIt, R.id.vOverlay -> {
                yapAnimate {
                }
            }
            R.id.fabAddMoney -> {
                yapAnimate {
                    openAddMoneyScreen()
                }
            }

            R.id.fabSendMoney, R.id.tvSendMoney -> {
                yapAnimate {
                    openSendMoneyScreen()
                }
            }
        }
    }

    private fun yapAnimate(endAnimation: () -> Unit) {
        setVisibility(viewModel.animClosed)
        setAnimation(viewModel.animClosed, endAnimation)
        viewModel.animClosed = !viewModel.animClosed;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimations()
        setupPager()
        //  setFeatureFlag()
        viewModelObservers()
        mViewBinding.bottomNavigationView.setOnItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
    }

    private fun setAnimations() {
        toBottom = AnimationUtils.loadAnimation(this, R.anim.pk_to_bottom_yap_anim).apply {
            duration = animDuration
            interpolator = AccelerateDecelerateInterpolator()
        }
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.pk_from_bottom_yap_anim).apply {
            duration = animDuration
            interpolator = AccelerateDecelerateInterpolator()
        }
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.pk_rotate_close_yap_anim).apply {
            duration = animDuration
            interpolator = AccelerateDecelerateInterpolator()
        }
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.pk_rotate_open_yap_anim).apply {
            duration = animDuration
            interpolator = AccelerateDecelerateInterpolator()
        }
        explosionOpen =
            AnimationUtils.loadAnimation(this, R.anim.pk_circle_explosion_yap_anim).apply {
                duration = animDuration
                interpolator = AccelerateDecelerateInterpolator()
            }
        explosionClose =
            AnimationUtils.loadAnimation(this, R.anim.pk_circle_explosion_in_yap_anim).apply {
                duration = animDuration
                interpolator = AccelerateDecelerateInterpolator()
            }
    }

    private fun setVisibility(closed: Boolean) {
        if (!closed) {
            mViewBinding.fabSendMoney.visible()
            mViewBinding.fabAddMoney.visible()
            mViewBinding.fabPayBills.visible()
            mViewBinding.tvSendMoney.visible()
            mViewBinding.tvPayBills.visible()
            mViewBinding.tvAddMoney.visible()
            mViewBinding.vCircle.visible()
        } else {
            mViewBinding.fabSendMoney.invisible()
            mViewBinding.fabAddMoney.invisible()
            mViewBinding.fabPayBills.invisible()
            mViewBinding.tvSendMoney.invisible()
            mViewBinding.tvAddMoney.invisible()
            mViewBinding.tvPayBills.invisible()
            mViewBinding.vCircle.invisible()
        }

    }

    private fun setFeatureFlag() {
        pkFeatureFlags.hasFeature(KEY_FLAG_YP_PK_PAY_BILLS) { hasFeature ->
            mViewBinding.fabPayBills.isEnabled = hasFeature
        }

        pkFeatureFlags.hasFeature(KEY_FLAG_YP_PK_ADD_MONEY) { hasFeature ->
            mViewBinding.fabAddMoney.isEnabled = hasFeature
        }

        pkFeatureFlags.hasFeature(KEY_FLAG_YP_PK_SEND_MONEY) { hasFeature ->
            mViewBinding.fabSendMoney.isEnabled = hasFeature
        }
    }


    private fun setAnimation(closed: Boolean, endAnimation: () -> Unit) {
        if (!closed) {
            mViewBinding.fabSendMoney.startAnimation(fromBottom)
            mViewBinding.tvSendMoney.startAnimation(fromBottom)
            mViewBinding.fabAddMoney.startAnimation(fromBottom)
            mViewBinding.tvAddMoney.startAnimation(fromBottom)
            mViewBinding.fabPayBills.startAnimation(fromBottom)
            mViewBinding.tvPayBills.startAnimation(fromBottom)
            mViewBinding.fabYapIt.startAnimation(rotateOpen)
            mViewBinding.vCircle.startAnimation(explosionOpen) {
                mViewBinding.vOverlay.visible()
                endAnimation()
            }
        } else {
            mViewBinding.fabSendMoney.startAnimation(toBottom)
            mViewBinding.tvSendMoney.startAnimation(toBottom)
            mViewBinding.fabAddMoney.startAnimation(toBottom)
            mViewBinding.tvAddMoney.startAnimation(toBottom)
            mViewBinding.fabPayBills.startAnimation(toBottom)
            mViewBinding.tvPayBills.startAnimation(toBottom)
            mViewBinding.fabYapIt.startAnimation(rotateClose)
            mViewBinding.vCircle.startAnimation(explosionClose) {
                mViewBinding.vOverlay.invisible()
                endAnimation()
            }
        }
    }

    private fun setupPager() {
        val adapter = DashboardPagerAdaptor(this)
        mViewBinding.mainViewPager.adapter = adapter
        with(mViewBinding.mainViewPager) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }
        mViewBinding.mainViewPager.isUserInputEnabled = false
        mViewBinding.mainViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mViewBinding.mainViewPager.registerOnPageChangeCallback(pageChanger)
    }

    private val mOnNavigationItemSelectedListener =
        object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.dashboard_nav_home -> {
                        mViewBinding.mainViewPager.setCurrentItem(0, false)
                        return true
                    }
                    R.id.dashboard_nav_store -> {
                        mViewBinding.mainViewPager.setCurrentItem(1, false)
                        return true
                    }

                    R.id.dashboard_nav_cards -> {
                        mViewBinding.mainViewPager.setCurrentItem(2, false)
                        return true
                    }
                    R.id.dashboard_nav_more -> {
                        mViewBinding.mainViewPager.setCurrentItem(3, false)
                        return true
                    }
                    else -> false
                }
            }
        }

    private val pageChanger = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.mainViewPager.unregisterOnPageChangeCallback(pageChanger)
    }

    override fun onBackPressed() {
        when {
            viewModel.isOverlyShowing() -> {
                viewModel.closeOverly().let { true }
            }
            mViewBinding.mainViewPager.currentItem != 0 -> {
                mViewBinding.bottomNavigationView.selectedItemId = R.id.dashboard_nav_home
            }
            else -> super.onBackPressed()
        }
    }

    override fun openAddMoneyScreen() {
        val intent = Intent(this, AddMoneyMainActivity::class.java)
        launchIntent(intent)
    }

    override fun openSendMoneyScreen() {
        val intent = Intent(this, SendMoneyDashboardMainActivity::class.java)
        launchIntent(intent)
    }

    private fun launchIntent(intent: Intent) {
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // To be handled
            }
        }
    }

    private fun handleCloseOverly(it: Boolean) {
        if (it) yapAnimate { }
    }

    private fun onDebitCardLoaded(card: Card) {
        viewModel.deeplinkHandler.onDataLoaded(this)
    }

    override fun viewModelObservers() {
        observe(viewModel.closeOverly, ::handleCloseOverly)
        observe(viewModel.debitCard, ::onDebitCardLoaded)
    }

}