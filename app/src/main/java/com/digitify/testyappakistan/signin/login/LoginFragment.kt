package com.digitify.testyappakistan.signin.login

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.KEY_IS_REMEMBER
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent
import com.digitify.testyappakistan.BR
import com.digitify.testyappakistan.DeeplinkIntentHandler
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.FragmentLoginBinding
import com.digitify.testyappakistan.onboarding.countryCodePicker.CountryItem
import com.digitify.testyappakistan.onboarding.countryCodePicker.CreateCountryAdapter
import com.digitify.testyappakistan.utils.Constants.PK_COUNTRY_CODE
import com.digitify.testyappakistan.utils.Constants.REGION
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_sign_in_display_text_error_text
import com.yap.yappk.pk.deeplinkNavigations.NotificationPayloadImpl
import com.yap.yappk.pk.ui.auth.main.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :
    BaseNavFragment<FragmentLoginBinding, ILogin.State, ILogin.ViewModel>
        (R.layout.fragment_login),
    ILogin.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ILogin.ViewModel by viewModels<LoginVM>()

    @Inject
    lateinit var sharedPreference: SharedPreferenceManager

    @Inject
    lateinit var adapter: CreateCountryAdapter

    @Inject
    lateinit var deeplinkIntentHandler: DeeplinkIntentHandler

    @Inject
    lateinit var notificationPayloadImpl: NotificationPayloadImpl

    private var startIcon: Drawable? = null

    private var countryBottomSheet: BottomSheetUIDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUserPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCountryBottomSheet()
        setTouchListener()
        startObservers()
        observeEvents()

        startIcon?.let { mViewBinding.tiMobileNumber.startIconDrawable = it }


    }

    private fun setUserPreference() {
        viewModel.viewState.isRemember.set(sharedPreference.getValueBoolien(KEY_IS_REMEMBER, false))
        if (viewModel.viewState.isRemember.get()) {
            viewModel.viewState.countryCode.set(
                sharedPreference.getDecryptedUserDialCode().toString()
            )
            viewModel.viewState.mobile.value = sharedPreference.getDecryptedUserName()
            startIcon =
                adapter.getAdapter(requireContext()).getDataList().find { countryItem ->
                    countryItem.code == viewModel.viewState.countryCode.get().toString()
                }?.iconStart
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        mViewBinding.etMobileNumber.setOnTouchListener(View.OnTouchListener { v, event ->
            val drawableLeft = 0
            if (event.action == MotionEvent.ACTION_UP && event.rawX <=
                mViewBinding.etMobileNumber.compoundDrawables[drawableLeft].bounds.width()
            ) {
                // your action here
                countryBottomSheet?.show(childFragmentManager, "country_bottom_sheet")
                return@OnTouchListener true
            }
            false
        })
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnLogin -> {
                val mobileNo = viewModel.viewState.mobile.value?.replace(" ", "") ?: ""
                val countryCode = viewModel.viewState.countryCode.get()
                viewModel.verifyUser(countryCode, mobileNo)
            }
            R.id.tvSignUp -> {
                navigateWithPopup(
                    R.id.action_loginFragment_to_accountSelectionFragment,
                    R.id.loginFragment
                )
            }
        }
    }

    private fun saveUserPreference() {
        sharedPreference.save(KEY_IS_REMEMBER, viewModel.viewState.isRemember.get())
        sharedPreference.saveUserNameWithEncryption(
            viewModel.viewState.mobile.value.toString().replace(" ", "")
        )
        sharedPreference.saveUserDialCodeWithEncryption(
            viewModel.viewState.countryCode.get().toString().replace(" ", "")
        )

    }

    private fun prepareCountryBottomSheet() {
        val countryAdapter = adapter.getAdapter(requireContext())
        countryAdapter.onItemClickListener = itemClickListener
        countryBottomSheet =
            BottomSheetBuilder().setAdapter(countryAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
                .build()
    }

    private val itemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            countryBottomSheet?.dismiss()
            if (data is CountryItem && pos > 0) {
                mViewBinding.tiMobileNumber.startIconDrawable = data.iconStart
                viewModel.viewState.countryCode.set(data.code.toString())

            }
        }
    }

    private fun startObservers() {
        viewModel.verifyUserEvent.observe(requireActivity(), {
            if (it) {
                saveUserPreference()
                mViewBinding.tiMobileNumber.error = null
                viewModel.openAuthenticationScreen()
            } else {
                viewModel.viewState.isError.set(true)
                mViewBinding.tiMobileNumber.error =
                    requireContext().getString(screen_sign_in_display_text_error_text)
            }
        })

        viewModel.isAccountBlocked.observe(requireActivity(), {
            if (it) {
                saveUserPreference()
                mViewBinding.tiMobileNumber.error = null
                viewModel.openAuthenticationScreen()
            }
        })
    }

    private fun observeEvents() {
        observeEvent(viewModel.openAuthentication, ::goToAuthentication)
    }

    private fun startAuthenticationActivity() {
        launchActivity<AuthenticationActivity>(clearPrevious = true) {
            putExtra("countryCode", viewModel.viewState.countryCode.get().toString().trim())
            putExtra("mobileNo", viewModel.viewState.mobile.value)
            putExtra("isAccountBlocked", viewModel.isAccountBlocked.value)
        }

    }

    private fun goToAuthentication(navigationEvent: SingleEvent<String>) {
        val hashMap = deeplinkIntentHandler.getDeeplinkIntentData(requireActivity().intent)
        val notificationRegionId = hashMap[REGION]

        navigationEvent.getContentIfNotHandled()?.let { countryCode ->
            when (countryCode) {
                "0092", "+92" -> {
                    if (deeplinkIntentHandler.isSameRegion(notificationRegionId, PK_COUNTRY_CODE))
                        notificationPayloadImpl.onReceivePayload(hashMap)

                    startAuthenticationActivity()
                }
            }
        }
    }
}