package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBanksListBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.banksearch.BankSearchFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary.FindBeneficiaryAccountFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.AddBeneficiaryVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.IAddBeneficiary
import com.yap.yappk.pk.utils.KEY_BANKS_LIST
import com.yap.yappk.pk.utils.cancelAllSnackBar
import com.yap.yappk.pk.utils.showTextUpdatedAbleSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BanksListFragment :
    BaseNavFragment<PkFragmentBanksListBinding, IBanksList.State, IBanksList.ViewModel>(R.layout.pk_fragment_banks_list),
    IBanksList.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IBanksList.ViewModel by viewModels<BanksListVM>()
    private val parentViewModel: IAddBeneficiary.ViewModel by activityViewModels<AddBeneficiaryVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.banksListRequest()
        setUpViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.toolBarVisibility.value = View.VISIBLE
        parentViewModel.stateList.value = viewModel.handleState()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.flSearch -> {
                setFragmentResult(
                    BankSearchFragment::class.java.name,
                    bundleOf(KEY_BANKS_LIST to viewModel.bankDataSuccess.value)
                )
                navigate(R.id.action_banksListFragment_to_bankSearchFragment)
            }
        }
    }

    private fun setUpViews() {
        viewModel.banksAdapter.onItemClickListener = rvItemClickListener
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is BankData -> {
                    viewModel.navigateToFetchAccountScreen(data)
                }
            }
        }
    }

    private fun setBanksList(list: List<BankData>) {
        viewModel.banksAdapter.setList(list)
        mViewBinding.rvBanks.adapter = viewModel.banksAdapter
    }

    private fun handleError(errorMessage: CharSequence) {
        if (errorMessage.isEmpty().not()) {
            showTextUpdatedAbleSnackBar(
                msg = errorMessage,
                viewBgColor = R.color.pkWarningBackground,
                colorOfMessage = R.color.pkWarning,
                gravity = Gravity.TOP
            )
        } else cancelAllSnackBar()
    }

    private fun navigateToFetchAccountScreen(bankData: SingleEvent<BankData?>) {
        bankData.getContentIfNotHandled()?.let {
            navigate(
                R.id.action_banksListFragment_to_findBeneficiaryAccountFragment, bundleOf(
                    FindBeneficiaryAccountFragment::class.java.name to it
                )
            )
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.bankDataSuccess, ::setBanksList)
        observe(viewModel.errorDescription, ::handleError)
        observeEvent(viewModel.openFetchAccountTitle, ::navigateToFetchAccountScreen)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAllSnackBar()
    }
}
