package com.digitify.testyappakistan.onboarding.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.launchActivity
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.digitify.testyappakistan.BR
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.FragmentMobileNoBinding
import com.digitify.testyappakistan.onboarding.countryCodePicker.CountryItem
import com.digitify.testyappakistan.onboarding.countryCodePicker.CreateCountryAdapter
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.yappk.pk.ui.onboarding.main.YapPkMainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MobileNoFragment :
    BaseFragment<FragmentMobileNoBinding, IMobileNo.State, IMobileNo.ViewModel>
        (R.layout.fragment_mobile_no),
    IMobileNo.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMobileNo.ViewModel by viewModels<MobileNoVM>()
    private lateinit var startOnboardingTime: Date

    @Inject
    lateinit var adapter: CreateCountryAdapter
    private var countryBottomSheet: BottomSheetUIDialog? = null

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                val mobileNumber = viewModel.viewState.mobile.value?.replace(" ", "")
                val countryCode: String =
                    viewModel.viewState.countryCode.get().toString().trim().replace("+", "00")

                viewModel.createOtpUserVerifier(countryCode, mobileNumber ?: "")
            }
            R.id.ivBack -> {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun startPkMainActivity(isOtpCreated: Boolean) {
        if (!isOtpCreated) return
        launchActivity<YapPkMainActivity> {
            putExtra(
                "countryCode",
                viewModel.viewState.countryCode.get().toString().trim()
            )
            putExtra("mobileNo", viewModel.viewState.mobile.value)
            putExtra("startOnboardingTime", startOnboardingTime.time)
        }
    }

    // Route method from supper-app on Country code
    private fun startMainActivity(isOtpCreated: Boolean) {
        if (!isOtpCreated) return
        if (viewModel.viewState.selectedRegion.value == "PK") {
            startPkMainActivity(isOtpCreated)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startOnboardingTime = Date()
        setObserver()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCountryBottomSheet()
        mViewBinding.etMobileNumber2.setOnTouchListener(OnTouchListener { v, event ->
            val drawableLeft = 0
            if (event.action == MotionEvent.ACTION_UP && event.rawX <=
                mViewBinding.etMobileNumber2.compoundDrawables[drawableLeft].bounds.width()
            ) {
                // your action here
                countryBottomSheet?.show(childFragmentManager, "country_bottom_sheet")
                return@OnTouchListener true
            }
            false
        })


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
                mViewBinding.etMobileNumber.startIconDrawable = data.iconStart
                viewModel.viewState.countryCode.set(data.code.toString())

            }
        }
    }

    private fun showErrorOnUi(errorEvent: SingleEvent<String?>) {
        val errorMessage = errorEvent.getContentIfNotHandled()
        mViewBinding.etMobileNumber.error = errorMessage
        mViewBinding.etMobileNumber.isErrorEnabled = errorMessage != null
    }

    private fun setObserver() {
        viewModel.viewState.selectedRegion
        observeEvent(viewModel.showError, ::showErrorOnUi)
        observe(viewModel.otpCreateEvent, ::startMainActivity)
    }
}