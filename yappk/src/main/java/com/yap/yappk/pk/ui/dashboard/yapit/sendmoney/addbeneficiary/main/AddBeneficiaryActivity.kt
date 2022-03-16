package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.extensions.hideKeyboard
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkActivityAddBeneficiaryBinding
import com.yap.yappk.localization.*
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBeneficiaryActivity :
    BaseNavActivity<PkActivityAddBeneficiaryBinding, IAddBeneficiary.State, IAddBeneficiary.ViewModel>(),
    IAddBeneficiary.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IAddBeneficiary.ViewModel by viewModels<AddBeneficiaryVM>()
    override fun getLayoutId(): Int = R.layout.pk_activity_add_beneficiary

    override val navigationGraphId: Int = R.navigation.pk_add_beneficiary_nav_graph

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
    }

    private fun setToolBarVisibility(visibility: Int) {
        mViewBinding.tbView.visibility = visibility
        mViewBinding.rvAddBeneficiaryState.visibility = visibility
    }

    override fun onEndTextClicked() {
        super.onEndTextClicked()
        showCancelAlert()
    }

    override fun onStartIconClicked() {
        super.onStartIconClicked()
        onBackPressed()
        hideKeyboard()
    }

    private fun showCancelAlert() {
        confirm(
            title = getString(common_text_exit_pop_up_heading),
            message = getString(
                common_text_exit_pop_up_description
            ),
            positiveButton = getString(common_button_confirm),
            negativeButton = getString(common_button_cancel),
            cancelable = false
        ) {
            finish()
        }
    }

    private fun setUpStateView(list: List<AddBeneficiaryStateModel>) {
        viewModel.statesAdapter.setList(list)
    }

    override fun viewModelObservers() {
        observe(viewModel.toolBarVisibility, ::setToolBarVisibility)
        observe(viewModel.stateList, ::setUpStateView)
    }
}