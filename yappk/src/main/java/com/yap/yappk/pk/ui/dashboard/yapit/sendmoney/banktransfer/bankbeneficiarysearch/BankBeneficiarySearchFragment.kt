package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarysearch

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentBankBeneficiarySearchBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter.BankBeneficiaryListAdapter
import com.yap.yappk.pk.utils.KEY_BANK_BENEFICIARY_LIST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankBeneficiarySearchFragment :
    BaseNavFragment<PkFragmentBankBeneficiarySearchBinding, IBankBeneficiarySearch.State, IBankBeneficiarySearch.ViewModel>(
        R.layout.pk_fragment_bank_beneficiary_search
    ), IBankBeneficiarySearch.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IBankBeneficiarySearch.ViewModel by viewModels<BankBeneficiarySearchVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        initSearchViewListener()
        getFragmentArguments()
    }

    private fun setUpRecyclerView() {
        viewModel.beneficiaryListAdapter =
            BankBeneficiaryListAdapter(
                list = mutableListOf(),
                resourcesProviders = viewModel.resourcesProviders,
                isSearchView = true
            )
        viewModel.beneficiaryListAdapter?.onItemClickListener = rvItemClickListener
        mViewBinding.rvBankBeneficiaries.adapter = viewModel.beneficiaryListAdapter
    }

    override fun initSearchViewListener() {
        mViewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.beneficiaryListAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    override fun getFragmentArguments() {
        setFragmentResultListener(BankBeneficiarySearchFragment::class.java.name) { requestKey: String, bundle: Bundle ->
            viewModel.setBeneficiaryList(
                bundle.getParcelableArrayList<IBeneficiary>(
                    KEY_BANK_BENEFICIARY_LIST
                ) ?: listOf()
            )
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvCancel -> {
                context?.hideKeyboard()
                navigateBack()
            }
        }
    }

    private fun handleBeneficiaries(list: List<IBeneficiary>) {
        viewModel.beneficiaryListAdapter?.setList(list)
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is IBeneficiary -> {
                    showToast("Transfer")
                }
            }
        }
    }


    override fun viewModelObservers() {
        observe(viewModel.bankBeneficiariesList, ::handleBeneficiaries)
    }
}