package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.share
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAccountDetailBinding
import com.yap.yappk.localization.common_text_copied
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_account_detail_button_share
import com.yap.yappk.pk.utils.copyToClipboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDetailFragment :
    BaseNavFragment<FragmentAccountDetailBinding, IAccountDetail.State, IAccountDetail.ViewModel>(
        R.layout.fragment_account_detail
    ),
    IAccountDetail.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IAccountDetail.ViewModel by viewModels<AccountDetailVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnShare ->
                requireContext().share(
                    viewModel.getShareBody(),
                    requireContext().getString(screen_account_detail_button_share)
                )
            R.id.tvIbanCopy -> {
                requireContext().copyToClipboard(viewModel.viewState.iban.value ?: "")
                showToast(requireContext().getString(common_text_copied))
            }

            R.id.tvAccountNumberCopy ->{
                requireContext().copyToClipboard(viewModel.viewState.accountNumber.value ?: "")
                showToast(requireContext().getString(common_text_copied))
            }
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }
}