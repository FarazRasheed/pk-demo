package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionfilter

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardTransactionFilterBinding
import com.yap.yappk.networking.microservices.transactions.responsedtos.FilterAmount
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.enums.FilterTransactionType
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardTransactionFilterFragment :
    BaseNavFragment<FragmentCardTransactionFilterBinding, ICardTransactionFilter.State, ICardTransactionFilter.ViewModel>(
        R.layout.fragment_card_transaction_filter
    ),
    ICardTransactionFilter.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardTransactionFilter.ViewModel by viewModels<CardTransactionFilterVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentListener()
        setValues()
        getFragmentArguments()
        viewModel.fetchFilterAmount()
    }

    private fun setValues() {
        mViewBinding.rsbAmount.setProgress(0f)
        mViewBinding.tvRangeTitle.text = viewModel.getRangeTitle()
    }

    private fun setFragmentListener() {
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.rsbAmount.setOnRangeChangedListener(viewModel.getChangeListener())
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.filterData =
                it.getParcelable<FilterExtras>(CardTransactionFilterFragment::class.java.name)
            setPreviousFilters()
        }

    }

    private fun setPreviousFilters() {
        if (viewModel.filterData?.isFilterSet == true) {
            mViewBinding.rsbAmount.setProgress(
                viewModel.filterData?.minRange?.toFloat() ?: 0f,
                viewModel.filterData?.maxRange?.toFloat() ?: 0f
            )
            setTransactionTypeFilter()
        }
    }

    private fun setTransactionTypeFilter() {
        viewModel.filterData?.txnType?.let { txnType ->
            when (txnType) {
                FilterTransactionType.ATM_WITHDRAW.name -> {
                    mViewBinding.cbRetails.isChecked = false
                }
                FilterTransactionType.POS.name -> {
                    mViewBinding.cbAtm.isChecked = false
                }
            }
        }
    }

    private fun handleFilterAmount(filterAmount: FilterAmount) {
        val min = filterAmount.minAmount?.toFloat() ?: 0f
        val max = filterAmount.maxAmount?.toFloat() ?: 0f
        val interval = (max - min).times(0.30).toFloat()
        mViewBinding.rsbAmount.setRange(
            min,
            max,
            interval
        )
        if (viewModel.filterData == null)
            mViewBinding.rsbAmount.setProgress(0f)
        else setPreviousFilters()
    }

    override fun onStartIconClicked() {
        navigateBackWithResult(
            resultCode = Activity.RESULT_CANCELED
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnApplyFilter -> {
                viewModel.setFilterData()
                navigateBackWithResult(
                    resultCode = Activity.RESULT_OK,
                    bundleOf(CardTransactionFilterFragment::class.java.name to viewModel.filterData)
                )
            }
            R.id.btnClearFilter -> {
                navigateBackWithResult(
                    resultCode = Activity.RESULT_OK
                )
            }
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.filterAmount, ::handleFilterAmount)
    }

}