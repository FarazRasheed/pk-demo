package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.utils.DateUtils
import com.digitify.core.utils.DateUtils.SERVER_DATE_FORMAT
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentYapToYapTransferSuccessBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_yap_to_yap_transfer_success_display_text_transfer_mobile_number
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.utils.YAP_TO_YAP_SUCCESS_MODEL
import com.yap.yappk.pk.utils.shareImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YapToYapTransferSuccessFragment :
    BaseNavFragment<PkFragmentYapToYapTransferSuccessBinding, IYapToYapTransferSuccess.State, IYapToYapTransferSuccess.ViewModel>(
        R.layout.pk_fragment_yap_to_yap_transfer_success
    ), ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IYapToYapTransferSuccess.ViewModel by viewModels<YapToYapTransferSuccessVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.contact.value =
            arguments?.getParcelable(YapToYapTransferSuccessFragment::class.java.name)

        viewModel.viewState.yapToYapTransaction.value =
            arguments?.getParcelable(YAP_TO_YAP_SUCCESS_MODEL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setBackButtonDispatcher()
        initViews()
    }

    private fun initViews() {
        mViewBinding.tvReceiverMobileNumber.text = requireContext().getString(
            screen_yap_to_yap_transfer_success_display_text_transfer_mobile_number,
            getFormattedPhoneNumber(
                requireContext(),
                viewModel.viewState.contact.value?.countryCde?.replace(
                    "+",
                    "00"
                ) + viewModel.viewState.contact.value?.mobileNumber
            )
        )
        val date = DateUtils.reformatStringDate(
            viewModel.viewState.yapToYapTransaction.value?.date ?: "",
            SERVER_DATE_FORMAT,
            DateUtils.FORMAT_LONG_OUTPUT
        )
        mViewBinding.tvDate.text = date
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnGoToDashboard -> {
                requireActivity().finish()
            }
        }
    }

    override fun onEndIconClicked() {
        requireContext().shareImage(
            mViewBinding.clShare,
            imageName = "shareYapPkImageName",
            shareText = "",
            chooserTitle = "",
            applicationId = pkBuildConfigurations.configManager?.applicationId ?: ""
        )
    }
}
