package com.yap.yappk.pk.ui.kyc.address.city

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.visible
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.multiStateView.Status
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCityNameBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.City
import com.yap.yappk.pk.ui.kyc.address.delivery.AddressSelectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityNameFragment :
    BaseNavFragment<FragmentCityNameBinding, ICityName.State, ICityName.ViewModel>(R.layout.fragment_city_name),
    ICityName.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ICityName.ViewModel by viewModels<CityNameVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.searchView -> {
                mViewBinding.searchView.isIconified = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchViewListener()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
    }

    override fun initSearchViewListener() {
        mViewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.citiesAdapter.filter.filter(newText)
                return false
            }
        })

        mViewBinding.searchView.setOnQueryTextFocusChangeListener { _, isFocused ->
            if (isFocused) {
                mViewBinding.searchView.isIconified = false
                mViewBinding.tvHintView.gone()
            } else {
                mViewBinding.searchView.isIconified = true
                mViewBinding.tvHintView.visible()
            }
        }
    }

    private fun handleCitiesList(citiesList: MutableList<City>) {
        viewModel.citiesAdapter.setList(citiesList)
        viewModel.citiesAdapter.allowFullItemClickListener = true
        viewModel.citiesAdapter.onItemClickListener = onItemClickListener
        mViewBinding.recyclerView.adapter = viewModel.citiesAdapter
    }

    private fun handleFilterCount(itemsCount: Int) = viewModel.onFilterCount(itemsCount)

    private fun handleViewState(state: MultiState) {
        when (state.status) {
            Status.LOADING -> mViewBinding.multiStateView.viewState = StateEnum.LOADING
            Status.EMPTY -> mViewBinding.multiStateView.viewState = StateEnum.EMPTY
            Status.ERROR -> mViewBinding.multiStateView.viewState = StateEnum.ERROR
            Status.SUCCESS -> mViewBinding.multiStateView.viewState = StateEnum.CONTENT
            else -> mViewBinding.multiStateView.viewState = StateEnum.CONTENT
        }
    }

    private fun onCitySelection(city: City) {
        navigateBackWithResult(
            1,
            bundleOf(AddressSelectionFragment::class.java.name to city.name)
        )
    }

    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is City) {
                onCitySelection(data)
            }
        }
    }

    override fun onStartIconClicked() = navigateBack()

    override fun viewModelObservers() {
        observe(viewModel.multiStateView, ::handleViewState)
        observe(viewModel.citiesList, ::handleCitiesList)
        observe(viewModel.citiesAdapter.filterCount, ::handleFilterCount)
    }
}