package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAddMoneyDashboardBinding
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main.AddMoneyMainVM
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main.IAddMoneyMain
import com.yap.yappk.pk.ui.dashboard.yapit.qrcode.main.MainQrActivity
import com.yap.yappk.pk.utils.ItemCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMoneyDashboardFragment :
    BaseNavFragment<FragmentAddMoneyDashboardBinding, IAddMoneyDashboard.State, IAddMoneyDashboard.ViewModel>(
        R.layout.fragment_add_money_dashboard
    ),
    IAddMoneyDashboard.View, ToolBarView.OnToolBarViewClickListener,
    ItemCard.OnCardItemClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IAddMoneyDashboard.ViewModel by viewModels<AddMoneyDashboardVM>()
    private val parentViewModel: IAddMoneyMain.ViewModel by activityViewModels<AddMoneyMainVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.icBankTransfer.setOnItemCardClickListener(this)
        mViewBinding.icCredit.setOnItemCardClickListener(this)
        mViewBinding.icQr.setOnItemCardClickListener(this)
    }

    override fun onClick(id: Int) {
        when (id) {
            // handle later
        }
    }

    override fun onItemClicked(view: View) {
        when (view.id) {
            R.id.icCredit -> openCardScreen()
            R.id.icBankTransfer -> openAccountDetailScreen()
            R.id.icQr -> openQrCodeScreen()
        }
    }

    private fun openQrCodeScreen() {
        val intent = Intent(requireContext(), MainQrActivity::class.java)
        intent.putExtra(
            NAVIGATION_GRAPH_START_DESTINATION_ID, R.id.generateQrFragment
        )
        activityLauncher.launch(intent)
    }


    override fun openAccountDetailScreen() {
        navigate(R.id.action_addMoneyDashboardFragment_to_accountDetailFragment)
    }

    override fun openCardScreen() {
        navigate(R.id.action_addMoneyDashboardFragment_to_cardDashboardFragment)
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }
}