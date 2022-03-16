package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.banksearch

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.extensions.hideKeyboard
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankSearchBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary.FindBeneficiaryAccountFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.AddBeneficiaryVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.IAddBeneficiary
import com.yap.yappk.pk.utils.KEY_BANKS_LIST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankSearchFragment :
    BaseNavFragment<PkFragmentBankSearchBinding, IBankSearch.State, IBankSearch.ViewModel>(
        R.layout.pk_fragment_bank_search
    ),
    IBankSearch.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IBankSearch.ViewModel by viewModels<BankSearchVM>()
    private val parentViewModel: IAddBeneficiary.ViewModel by activityViewModels<AddBeneficiaryVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel.toolBarVisibility.value = View.GONE
        viewModelObservers()
        getFragmentArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        initSearchViewListener()
    }

    override fun getFragmentArguments() {
        setFragmentResultListener(BankSearchFragment::class.java.name) { requestKey: String, bundle: Bundle ->
            viewModel.setBanksList(
                bundle.getParcelableArrayList(
                    KEY_BANKS_LIST
                ) ?: listOf()
            )
        }
    }

    private fun setUpViews() {
        viewModel.banksListAdapter?.onItemClickListener = rvItemClickListener
    }

    override fun initSearchViewListener() {
        mViewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.banksListAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnCancel -> {
                navigateUp()
            }
        }
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is BankData -> {
                    navigateWithPopup(
                        R.id.action_bankSearchFragment_to_findBeneficiaryAccountFragment,
                        R.id.bankSearchFragment,
                        bundleOf(
                            FindBeneficiaryAccountFragment::class.java.name to data
                        )
                    )
                }
            }
        }
    }

    private fun setUpListData(list: List<BankData>) {
        viewModel.banksListAdapter?.setList(list)
    }

    override fun viewModelObservers() {
        observe(viewModel.banksList, ::setUpListData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }
}
