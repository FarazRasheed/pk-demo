package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentGlobalSearchBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch.adapter.BeneficiaryListAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main.YapToYapTransferFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalSearchFragment :
    BaseNavFragment<FragmentGlobalSearchBinding, IGlobalSearch.State, IGlobalSearch.ViewModel>(
        R.layout.fragment_global_search
    ), IGlobalSearch.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IGlobalSearch.ViewModel by viewModels<GlobalSearchVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        initSearchViewListener()
        if (viewModel.beneficiariesList.value == null)
            viewModel.getLocalContacts()
        else
            handleBeneficiaries(viewModel.beneficiariesList.value ?: listOf())
    }

    private fun setUpRecyclerView() {
        viewModel.beneficiaryListAdapter =
            BeneficiaryListAdapter(
                list = mutableListOf(),
                resourcesProviders = viewModel.resourcesProviders
            )
        viewModel.beneficiaryListAdapter?.onItemClickListener = rvItemClickListener
        mViewBinding.rvYapContacts.adapter = viewModel.beneficiaryListAdapter
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

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvCancel -> {
                context?.hideKeyboard()
                navigateBack()
            }
        }
    }

    private fun handleLocalPhoneContacts(list: List<IBeneficiary>) {
        viewModel.getBeneficiariesFrom(list as List<Contact>)
    }

    private fun handleBeneficiaries(list: List<IBeneficiary>) {
        viewModel.beneficiaryListAdapter?.setList(list)
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is IBeneficiary -> {
                    if (data.isYapUser) {
                        openY2YTransferScreen(data as IBeneficiary)
                    }
                }
            }
        }
    }

    private fun openY2YTransferScreen(contact: IBeneficiary) {
        navigate(
            R.id.action_globalSearchFragment_to_pk_y2y_transfer_nav,
            bundleOf(YapToYapTransferFragment::class.java.name to contact)
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.localContacts, ::handleLocalPhoneContacts)
        observe(viewModel.beneficiariesList, ::handleBeneficiaries)
    }
}