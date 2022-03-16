package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.Dispatcher
import com.digitify.core.extensions.hideKeyboard
import com.google.android.material.tabs.TabLayoutMediator
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentContactSearchBinding
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.ISendMoneyDashboardMain
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.SendMoneyDashboardMainVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.ISendToYAPMain
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.SendToYAPMainVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.adapter.ContactsPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ContactSearchFragment :
    BaseNavFragment<FragmentContactSearchBinding, IContactSearch.State, IContactSearch.ViewModel>(
        R.layout.fragment_contact_search
    ),
    IContactSearch.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IContactSearch.ViewModel by viewModels<ContactSearchVM>()
    private val parentFragmentViewModel: ISendToYAPMain.ViewModel by viewModels<SendToYAPMainVM>(
        ownerProducer = { requireParentFragment() })
    private val parentActivityViewModel: ISendMoneyDashboardMain.ViewModel by activityViewModels<SendMoneyDashboardMainVM>()


    override var pagerSelectedPosition: Int = -1

    private var pagerAdapter: ContactsPagerAdapter? = null

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvCancel -> {
                hideKeyboard()
                navigateBack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPagerAdapter()
        initSearchViewListener()
    }

    override fun initSearchViewListener() {
        mViewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pagerAdapter?.searchContact(newText ?: "")
                return false
            }
        })
    }

    private fun setUpPagerAdapter() {
        pagerAdapter =
            ContactsPagerAdapter(
                requireActivity(),
                phoneContacts = parentActivityViewModel.phoneContacts.value ?: listOf(),
                yapContacts = parentActivityViewModel.yapContacts.value ?: listOf(),
                isSearch = true
            ) { pos: Int ->

            }
        launch(Dispatcher.Background) {
            delay(200)
            withContext(Dispatchers.Main) {
                mViewBinding.vpContacts.offscreenPageLimit = 1
                mViewBinding.searchView.isIconified = false
            }
        }
        mViewBinding.vpContacts.adapter = pagerAdapter
        mViewBinding.vpContacts.isUserInputEnabled = true
        TabLayoutMediator(
            mViewBinding.tlContacts,
            mViewBinding.vpContacts
        ) { tab, position ->
            tab.text = viewModel.getTabTitle(position)
        }.attach()
        handleViewPagerCallback()
        getFragmentArguments()
    }

    private fun getFragmentArguments() {
        arguments?.let {
            mViewBinding.vpContacts.setCurrentItem(
                it.getInt(
                    ContactSearchFragment::class.java.name, 0
                ), false
            )
        }
    }

    private fun handleViewPagerCallback() {
        mViewBinding.vpContacts.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pagerSelectedPosition = position
            }
        })
    }


    override fun viewModelObservers() = Unit
}
