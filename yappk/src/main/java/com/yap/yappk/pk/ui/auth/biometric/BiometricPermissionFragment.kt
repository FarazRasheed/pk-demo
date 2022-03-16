package com.yap.yappk.pk.ui.auth.biometric

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.clickableSpan
import com.digitify.core.extensions.getColors
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.BIO_METRIC_SCREEN_TYPE
import com.digitify.core.utils.KEY_IS_FINGERPRINT_PERMISSION_SHOWN
import com.digitify.core.utils.NOTIFICATION_SCREEN_TYPE
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.SCREEN_TYPE
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentPermissionSettingsBinding
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BiometricPermissionFragment :
    BaseNavFragment<FragmentPermissionSettingsBinding, IBiometricPermission.State, IBiometricPermission.ViewModel>(
        R.layout.fragment_permission_settings
    ) {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IBiometricPermission.ViewModel by viewModels<BiometricPermissionVM>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.screenType.value = arguments?.getString(BIO_METRIC_SCREEN_TYPE)
        viewModel.sharedPreferenceManager.save(KEY_IS_FINGERPRINT_PERMISSION_SHOWN, true)
        viewModel.setupViews()
        initTermAndCondition()
    }

    private fun initTermAndCondition() {
        mViewBinding.tvTermsCondition.clickableSpan(
            Pair(first = "Terms & Conditions", second = termAndConditionClickListener),
            color = requireContext().getColors(R.color.pkBlueWithAHintOfPurple)
        )
    }

    private val termAndConditionClickListener = View.OnClickListener {
        val bundle = Bundle()
        bundle.putString("PAGE_URL", "https://www.yap.com/terms")
        navigate(R.id.action_biometricPermissionFragment_to_webViewFragment, bundle)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirmation -> {
                viewModel.saveUserSettings(true)
                viewModel.navigate()
            }
            R.id.tvNoThanks -> {
                viewModel.saveUserSettings(false)
                viewModel.navigate()
            }
        }
    }

    private fun openNotificationSettingScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId, extras = bundleOf(SCREEN_TYPE to NOTIFICATION_SCREEN_TYPE))
        }
    }

    private fun openDashboardActivity(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let {
            launchActivity<PkDashboardActivity>(clearPrevious = true)
        }
    }

    private fun viewModelObservers() {
        observeEvent(viewModel.openNotification, ::openNotificationSettingScreen)
        observeEvent(viewModel.openDashboard, ::openDashboardActivity)
    }

}
