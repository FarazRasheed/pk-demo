package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentMainBankTransferBinding
import com.yap.yappk.localization.common_button_cancel
import com.yap.yappk.localization.common_button_delete
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_main_bank_transfer_display_text_pop_up_description
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.AddBeneficiaryActivity
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarysearch.BankBeneficiarySearchFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.swipe.SwipeController
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.swipe.SwipeControllerActions
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.transfer.BankTransferFragment
import com.yap.yappk.pk.utils.BANK_BENEFICIARY_DETAIL_CODE
import com.yap.yappk.pk.utils.BANK_BENEFICIARY_LIST_CODE
import com.yap.yappk.pk.utils.KEY
import com.yap.yappk.pk.utils.KEY_BANK_BENEFICIARY_LIST
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainBankTransferFragment :
    BaseNavFragment<PkFragmentMainBankTransferBinding, IMainBankTransfer.State, IMainBankTransfer.ViewModel>(
        R.layout.pk_fragment_main_bank_transfer
    ),
    IMainBankTransfer.View, ToolBarView.OnToolBarViewClickListener, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IMainBankTransfer.ViewModel by viewModels<MainBankTransferVM>()
    private val swipeController = SwipeController()
    private var itemTouchHelper: ItemTouchHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.getBeneficiaries()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        if (viewModel.bankBeneficiariesList.value != null) {
            handleBeneficiaries(viewModel.bankBeneficiariesList.value ?: listOf())
        }
    }

    private fun setSwipeController() {
        swipeController.swipeController(swipeControllerActions)
        itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper?.attachToRecyclerView(mViewBinding.rvBeneficiaries)
        mViewBinding.rvBeneficiaries.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun onEndIconClicked() {
        openAddBankBeneficiaryScreen()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.clSearch -> {
                openBankBeneficiarySearchScreen()
            }
            R.id.tvRecentTransfer, R.id.hideRecentText -> {
                handleRecentVisibility()
            }
        }
    }

    private fun handleRecentVisibility() {
        viewModel.viewState.isRecentTransfersVisible.value =
            mViewBinding.hideRecentText.visibility == View.VISIBLE
        mViewBinding.rvRecentBeneficiaries.visibility =
            if (mViewBinding.hideRecentText.visibility == View.GONE) View.VISIBLE else View.GONE
        mViewBinding.vLine.visibility =
            if (mViewBinding.hideRecentText.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun openBankBeneficiarySearchScreen() {
        setFragmentResult(
            BankBeneficiarySearchFragment::class.java.name,
            bundleOf(KEY_BANK_BENEFICIARY_LIST to viewModel.bankBeneficiariesList.value)
        )
        navigateForResult(
            requestCode = BANK_BENEFICIARY_LIST_CODE,
            resId = R.id.action_mainBankTransferFragment_to_bankBeneficiarySearchFragment
        )
    }

    private fun handleRecentBeneficiaries(list: List<IBeneficiary>) {
        if (!list.isNullOrEmpty())
            viewModel.recentTransferAdapter.setList(list)
        else
            viewModel.viewState.isNoRecentTransfers.value = true
    }

    private fun handleBeneficiaries(list: List<IBeneficiary>) {
        viewModel.viewState.isBeneficiariesAvailable.value = !list.isNullOrEmpty()
        viewModel.bankBeneficiaryAdapter =
            BankBeneficiaryListAdapter(
                list = list.toMutableList(),
                resourcesProviders = viewModel.resourcesProvider
            )
        viewModel.bankBeneficiaryAdapter?.onItemClickListener = rvItemClickListener
        mViewBinding.rvBeneficiaries.adapter = viewModel.bankBeneficiaryAdapter
        if (!list.isNullOrEmpty()) setSwipeController()
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is IBeneficiary -> {
                    navigate(
                        R.id.action_mainBankTransferFragment_to_bankTransferFragment,
                        bundleOf(BankTransferFragment::class.java.name to data, KEY to pos)
                    )
                }
                is String -> {
                    openAddBankBeneficiaryScreen()
                }
            }
        }
    }

    private val swipeControllerActions = object : SwipeControllerActions {
        override fun onLeftClicked(position: Int) {
            openDetailFragment(
                (viewModel.bankBeneficiaryAdapter?.getDataForPosition(
                    position
                ) as IBeneficiary)
            )
        }

        override fun onRightClicked(position: Int) {
            confirm(
                message = requireContext().getString(
                    screen_main_bank_transfer_display_text_pop_up_description
                ),
                positiveButton = requireContext().getString(common_button_delete),
                negativeButton = requireContext().getString(common_button_cancel),
                cancelable = false
            ) {
                viewModel.removeBeneficiary(
                    (viewModel.bankBeneficiaryAdapter?.getDataForPosition(
                        position
                    ) as IBeneficiary).beneficiaryId ?: ""
                )
            }
        }
    }

    private fun openDetailFragment(iBeneficiary: IBeneficiary) {
        navigateForResult(
            resId = R.id.action_mainBankTransferFragment_to_bankBeneficiaryDetailFragment,
            args = bundleOf(IBeneficiary::class.java.name to iBeneficiary),
            requestCode = BANK_BENEFICIARY_DETAIL_CODE
        )
    }

    private fun openAddBankBeneficiaryScreen() {
        activityLauncher.launch(Intent(requireContext(), AddBeneficiaryActivity::class.java)) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getBeneficiaries()
            }
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.resultCode == Activity.RESULT_OK && result.requestCode == BANK_BENEFICIARY_LIST_CODE) viewModel.getBeneficiaries()
        if (result.resultCode == Activity.RESULT_OK && result.requestCode == BANK_BENEFICIARY_DETAIL_CODE) viewModel.getBeneficiaries()
    }

    private fun handleDeleteBeneficiaries(isDeleted: Boolean) {
        if (isDeleted) viewModel.getBeneficiaries()
    }

    override fun viewModelObservers() {
        observe(viewModel.recentBeneficiariesList, ::handleRecentBeneficiaries)
        observe(viewModel.bankBeneficiariesList, ::handleBeneficiaries)
        observe(viewModel.isBeneficiaryDeleted, ::handleDeleteBeneficiaries)
    }

}