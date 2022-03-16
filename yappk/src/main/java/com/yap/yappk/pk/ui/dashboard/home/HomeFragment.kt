package com.yap.yappk.pk.ui.dashboard.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finishAffinity
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observe
import com.digitify.core.utils.KEY_APP_UUID
import com.digitify.core.utils.KEY_IS_FINGERPRINT_PERMISSION_SHOWN
import com.digitify.core.utils.KEY_TOUCH_ID_ENABLED
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentHomeBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.EventManager
import com.yap.yappk.pk.ui.dashboard.main.DashboardVM
import com.yap.yappk.pk.ui.dashboard.main.IDashboard
import com.yap.yappk.pk.ui.kyc.KYCRouteManager
import com.yap.yappk.pk.ui.kyc.KycRoute
import com.yap.yappk.pk.ui.kyc.main.KycDashboardActivity
import com.yap.yappk.pk.utils.enums.PkAppEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    BaseNavFragment<FragmentHomeBinding, IHome.State, IHome.ViewModel>(R.layout.fragment_home),
    IHome.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: HomeVM by viewModels()
    private val parentViewModel: IDashboard.ViewModel by activityViewModels<DashboardVM>()


    @Inject
    lateinit var eventManager: EventManager

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnLogout -> callLogoutAPI()
            R.id.btnCompleteVerification -> {
                launchActivity<KycDashboardActivity>()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
        initToggleListener()
        mViewBinding.swTouchId.isChecked = isTouchIdEnabled()

        val kycRouteManager = KYCRouteManager()
        mViewBinding.btnCompleteVerification.visibility =
            when (kycRouteManager.getKycScreenRoute(viewModel.sessionManager.userAccount.value)) {
                KycRoute.ON_BOARDED -> View.VISIBLE
                KycRoute.SECRET_QUESTION_SCREEN -> View.VISIBLE
                KycRoute.SELFIE_SCREEN -> View.VISIBLE
                KycRoute.CARD_SCHEME_SCREEN -> View.VISIBLE
                KycRoute.NONE -> View.GONE
            }
    }

    private fun initToggleListener() {
        mViewBinding.swTouchId.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.sharedPreferenceManager.save(
                    KEY_IS_FINGERPRINT_PERMISSION_SHOWN,
                    true
                )
                viewModel.sharedPreferenceManager.save(KEY_TOUCH_ID_ENABLED, true)
            } else {
                viewModel.sharedPreferenceManager.save(
                    KEY_TOUCH_ID_ENABLED,
                    false
                )
            }
        }
    }

    private fun isTouchIdEnabled(): Boolean {
        return viewModel.sharedPreferenceManager.getValueBoolien(
            KEY_TOUCH_ID_ENABLED,
            false
        )
    }

    override fun onBackPressed(): Boolean = if (parentViewModel.isOverlyShowing()) {
        parentViewModel.closeOverly().let { true }
    } else
        showLogoutDialog().let { true }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.PkAlertDialogTheme)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("CONFIRM") { dialog, which ->
                finishAffinity()
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun callLogoutAPI() {
        val accountUUID = viewModel.sharedPreferenceManager.getValueString(
            KEY_APP_UUID
        ).toString()
        viewModel.logout(accountUUID) {
            eventManager.invokeEvent(PkAppEvent.LOGOUT)
            requireActivity().finish()
        }
    }

    fun handleDebitCard(card: Card) {

    }

    override fun viewModelObservers() {
        observe(parentViewModel.debitCard, ::handleDebitCard)
    }

}