package com.yap.yappk.pk.ui.auth.loginpasscode

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.biometric.BiometricUtils
import com.digitify.core.biometric.showBiometricDialog
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.*
import com.yap.uikit.widget.dialerpad.KeyClickListener
import com.yap.uikit.widget.dialerpad.KeyEvent
import com.yap.uikit.widget.dialerpad.OnSecureCodeListener
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentLoginPasscodeBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_verify_passcode_text_account_locked
import com.yap.yappk.networking.microservices.authentication.requestdtos.LoginRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.networking.microservices.messages.requestdtos.ForgotPasscodeOtpRequest
import com.yap.yappk.pk.EventManager
import com.yap.yappk.pk.ui.auth.AccountRoute
import com.yap.yappk.pk.ui.auth.AccountRouteManager
import com.yap.yappk.pk.ui.auth.main.AuthViewModel
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import com.yap.yappk.pk.ui.onboarding.main.YapPkMainActivity
import com.yap.yappk.pk.utils.enums.PkAppEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginPasscodeFragment :
    BaseNavFragment<FragmentLoginPasscodeBinding, ILoginPassCode.State, ILoginPassCode.ViewModel>(R.layout.fragment_login_passcode),
    OnSecureCodeListener, ILoginPassCode.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ILoginPassCode.ViewModel by viewModels<LoginPasscodeVM>()
    private val parentViewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var biometricUtils: BiometricUtils

    @Inject
    lateinit var eventManager: EventManager

    @Inject
    lateinit var accountRouteManager: AccountRouteManager

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivBack -> onBackPressed()
            R.id.btnSignIn -> onClickLogin()
            R.id.tvForgotPassword -> onClickForgotPasscode()
        }
    }

    private fun onClickForgotPasscode() {
        val request = ForgotPasscodeOtpRequest(
            destination = parentViewModel.mobileNo.replace(" ", ""),
            emailOTP = false
        )
        viewModel.createForgotOtp(request)
    }

    override fun onClickLogin() {
        val request = LoginRequest(
            clientId = parentViewModel.mobileNo.replace(" ", ""),
            clientSecret = viewModel.viewState.passcode.value,
            deviceId = viewModel.viewState.deviceId
        )
        viewModel.login(request)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sharedPreferenceManager.getValueString(KEY_APP_UUID)?.let {
            viewModel.viewState.deviceId = it
        } ?: showToast("Invalid UUID found")
        setRouteEventsObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setBackButtonDispatcher()
    }

    private fun init() {
        initPasscodeView()
        val has = biometricUtils.hasBioMetricFeature(requireActivity())
        viewModel.viewState.biometricEnable.value = viewModel.isBiometricLoginEnabled(has)

        if (parentViewModel.viewState.isAccountBlocked) {
            viewModel.blockAccount(
                requireContext().getString(
                    screen_verify_passcode_text_account_locked
                )
            )
        }
    }

    private fun initPasscodeView() {
        mViewBinding.keyBoard.setOnSecureCodeListener(this)
        mViewBinding.keyBoard.setKeyClickListener(onPasscodeKeyListener)
        viewModel.viewState.passcodeError.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty())
                AnimationUtils.startAnimation(
                    requireContext(),
                    mViewBinding.codeView,
                    R.anim.shake
                )
        })
    }

    private val onPasscodeKeyListener = object : KeyClickListener {
        override fun onKeyClicked(view: View, which: KeyEvent) {
            when (which) {
                KeyEvent.LEFT -> {
                    mViewBinding.keyBoard.delete()
                    mViewBinding.codeView.delete()
                }
                KeyEvent.RIGHT -> {
                    showBiometricPrompt()
                }
                else -> {
                    mViewBinding.keyBoard.input(which.value)
                    mViewBinding.codeView.input(which.value)
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        showBiometricDialog { isSuccess ->
            if (isSuccess) {
                resetPasscode()
                viewModel.viewState.passcode.value = viewModel.getClientSecret()
                viewModel.viewState.passcode.value?.forEach {
                    mViewBinding.codeView.input(it.toString())
                    mViewBinding.keyBoard.input(it.toString())
                }
                onClickLogin()
            }
        }
    }


    private fun handleScreenRoute(account: AccountInfo?) {
        when (accountRouteManager.getAccountRoute(account)) {
            AccountRoute.WAITING -> viewModel.openWaitingListScreen()
            AccountRoute.ALLOWED -> viewModel.openAllowedUserScreen()
            AccountRoute.BIO_METRIC -> viewModel.openBiometricSettingScreen()
            AccountRoute.OTP_BLOCKED -> {
                // to be handle later
            }
            AccountRoute.DASHBOARD -> viewModel.openDashboardScreen()
            AccountRoute.NONE -> {
                // to be handle later
            }
        }
    }

    private fun openWaitingScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            launchActivity<YapPkMainActivity>(
                options = Bundle(),
                clearPrevious = true
            ) {
                putExtra(NAVIGATION_GRAPH_ID, R.navigation.pk_onboarding_nav_graph)
                putExtra(
                    NAVIGATION_GRAPH_START_DESTINATION_ID,
                    destinationId
                )
            }
        }
    }

    private fun openAllowedScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigateWithPopup(destinationId, R.id.loginPasscodeFragment)
        }
    }

    private fun openOTPVerificationScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId)
        }
    }

    private fun openForgotOTPVerificationScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(
                destinationId,
                bundleOf(
                    LoginPasscodeFragment::class.java.name to parentViewModel.countryCode.replace(
                        "+",
                        "00"
                    ) + parentViewModel.mobileNo.replace(
                        " ",
                        ""
                    )
                )
            )
        }
    }

    private fun openPermissionSettingScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            val has = biometricUtils.hasBioMetricFeature(requireContext())
            if (has)
                navigate(
                    destinationId,
                    bundleOf(BIO_METRIC_SCREEN_TYPE to TOUCH_ID_SCREEN_TYPE)
                )
            else
                navigate(
                    destinationId,
                    bundleOf(BIO_METRIC_SCREEN_TYPE to NOTIFICATION_SCREEN_TYPE)
                )
        }
    }

    private fun openDashboardActivity(navigateEvent: SingleEvent<AccountInfo?>) {
        navigateEvent.getContentIfNotHandled()?.let {
            launchActivity<PkDashboardActivity>(clearPrevious = true)
        }
    }

    private fun handleDeviceValidation(isValidate: Boolean) {
        if (isValidate) {
            handleScreenRoute(viewModel.sessionManager.userAccount.value)
        } else {
            parentViewModel.passcode = viewModel.viewState.passcode.value ?: ""
            viewModel.openOTPVerificationScreen()
        }

    }

    private fun handleForgotOtpCreation(isForgotOtpCreated: Boolean) {
        if (isForgotOtpCreated) {
            viewModel.openForgotOTPVerificationScreen()
        }
    }

    private fun setRouteEventsObservers() {
        observe(viewModel.isDeviceValidate, ::handleDeviceValidation)
        observe(viewModel.isForgotOtpCreated, ::handleForgotOtpCreation)
        observeEvent(viewModel.openPermissionSettings, ::openPermissionSettingScreen)
        observeEvent(viewModel.openWaitingList, ::openWaitingScreen)
        observeEvent(viewModel.openAllowedUser, ::openAllowedScreen)
        observeEvent(viewModel.openOTPVerification, ::openOTPVerificationScreen)
        observeEvent(viewModel.openDashboard, ::openDashboardActivity)
        observeEvent(viewModel.openForgotOTPVerification, ::openForgotOTPVerificationScreen)
    }

    private fun doLogout() {
        if (viewModel.sessionManager.userAccount.value != null) {
            callLogoutAPI()
        } else {
            viewModel.sessionManager.expireUserSession()
            eventManager.invokeEvent(PkAppEvent.LOGOUT)
            requireActivity().finish()
        }
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

    override fun onBackPressed(): Boolean {
        doLogout()
        return true
    }

    override fun onResume() {
        super.onResume()
        resetPasscode()
    }

    private fun resetPasscode() {
        mViewBinding.codeView.clearCode()
        mViewBinding.keyBoard.clearCode()
    }

    override fun onCodeChange(code: String?) {
        super.onCodeChange(code)
        viewModel.viewState.passcodeError.value = null
        viewModel.viewState.passcode.value = code
    }

    override fun onCodeCompleted(code: String?, isCompleted: Boolean) {
        super.onCodeCompleted(code, isCompleted)
        viewModel.viewState.passcode.value = code
        viewModel.viewState.valid.value = isCompleted
    }
}
