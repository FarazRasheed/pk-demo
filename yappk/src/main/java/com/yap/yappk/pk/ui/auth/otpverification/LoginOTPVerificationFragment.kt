package com.yap.yappk.pk.ui.auth.otpverification

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.biometric.BiometricUtils
import com.digitify.core.extensions.*
import com.digitify.core.utils.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.otptextview.OTPListener
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentLoginOtpVerificationBinding
import com.yap.yappk.networking.microservices.authentication.requestdtos.DemographicDataRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.auth.AccountRoute
import com.yap.yappk.pk.ui.auth.AccountRouteManager
import com.yap.yappk.pk.ui.auth.main.AuthViewModel
import com.yap.yappk.pk.ui.dashboard.main.PkDashboardActivity
import com.yap.yappk.pk.ui.onboarding.main.YapPkMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class LoginOTPVerificationFragment :
    BaseNavFragment<FragmentLoginOtpVerificationBinding, ILoginOTPVerification.State, ILoginOTPVerification.ViewModel>(
        R.layout.fragment_login_otp_verification
    ), ILoginOTPVerification.View, MySMSBroadcastReceiver.OnSmsReceiveListener, OTPListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ILoginOTPVerification.ViewModel by viewModels<LoginOTPVerificationVM>()
    private val parentViewModel: AuthViewModel by activityViewModels()
    private var intentFilter: IntentFilter? = null
    private var appSMSBroadcastReceiver: MySMSBroadcastReceiver? = null

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var accountRouteManager: AccountRouteManager

    @Inject
    lateinit var biometricUtils: BiometricUtils

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnVerifyOtp -> onOTPVerifyClick()
            R.id.btnResend -> onResendOTPClick()
            R.id.ivBack -> requireActivity().onBackPressed()
        }
    }

    override fun onOTPVerifyClick() {
        val deviceId: String? =
            viewModel.sharedPreferenceManager.getValueString(KEY_APP_UUID)
        val verifyOtpRequest = DemographicDataRequest(
            clientId = viewModel.mobileNumber.replace(" ", ""),
            clientSecret = parentViewModel.passcode,
            deviceId = deviceId,
            otp = viewModel.otp
        )

        val demoGraphicsRequest = DemographicDataRequest(
            "LOGIN",
            Build.VERSION.RELEASE,
            deviceId,
            Build.BRAND,
            if (UtilityFunctions().isEmulator()) "generic" else Build.MODEL,
            "Android"
        )
        viewModel.verifyOtp(verifyOtpRequest, demoGraphicsRequest)
    }

    override fun onResendOTPClick() {
        val deviceId: String? =
            viewModel.sharedPreferenceManager.getValueString(KEY_APP_UUID)
        val request = DemographicDataRequest(
            clientId = viewModel.mobileNumber.replace(" ", ""),
            clientSecret = parentViewModel.passcode,
            deviceId = deviceId
        )
        viewModel.resendOtp(request)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        mViewBinding.otpView.otpListener = this
        launch(Dispatcher.Main) {
            delay(200)
            mViewBinding.otpView[0].showKeyboard()
        }
    }

    private fun initBroadCastForSMS() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        appSMSBroadcastReceiver = MySMSBroadcastReceiver(this)
    }

    private fun init() {
        initBroadCastForSMS()
        viewModel.countryCode = parentViewModel.countryCode
        viewModel.mobileNumber = parentViewModel.mobileNo
        viewModel.viewState.formattedNumber.value = getFormattedPhoneNumber(
            requireContext(),
            viewModel.countryCode.replace("+", "00") + viewModel.mobileNumber
        )
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
            navigateWithPopup(destinationId, R.id.loginOTPVerificationFragment)
        }
    }

    private fun openPermissionSettingScreen(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let { destinationId ->
            val has = biometricUtils.hasBioMetricFeature(requireContext())
            if (has)
                navigateWithPopup(
                    destinationId,
                    R.id.loginOTPVerificationFragment,
                    bundleOf(BIO_METRIC_SCREEN_TYPE to TOUCH_ID_SCREEN_TYPE)
                )
            else
                navigateWithPopup(
                    destinationId,
                    R.id.loginOTPVerificationFragment,
                    bundleOf(BIO_METRIC_SCREEN_TYPE to NOTIFICATION_SCREEN_TYPE)
                )
        }
    }

    private fun openDashboardActivity(navigateEvent: SingleEvent<AccountInfo?>) {
        navigateEvent.getContentIfNotHandled()?.let { account ->
            launchActivity<PkDashboardActivity>(clearPrevious = true)
        }
    }

    private fun handleOtpVerified(isVerified: Boolean) {
        if (isVerified) {
            handleScreenRoute(sessionManager.userAccount.value)
        } else {
            mViewBinding.otpView.setOTP("")
        }
    }

    private fun setObservers() {
        observe(viewModel.otpVerifiedEvent, ::handleOtpVerified)
        observeEvent(viewModel.openPermissionSettings, ::openPermissionSettingScreen)
        observeEvent(viewModel.openWaitingList, ::openWaitingScreen)
        observeEvent(viewModel.openAllowedUser, ::openAllowedScreen)
        observeEvent(viewModel.openDashboard, ::openDashboardActivity)
    }

    override fun onReceive(code: Intent?) {
        code?.let {
            it.resolveActivity(requireContext().packageManager)?.run {
            }
        }
    }

    override fun onInteractionListener() {
        viewModel.viewState.isValid.value = false
    }

    override fun onOTPComplete(otp: String) {
        viewModel.otp = otp
        viewModel.viewState.isValid.value = true
        onOTPVerifyClick()
    }

}