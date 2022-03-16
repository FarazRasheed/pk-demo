package com.yap.yappk.pk.ui.dashboard.cards.reorder.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkActivityReorderCardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReorderCardActivity :
    BaseNavActivity<PkActivityReorderCardBinding, IReorderCard.State, IReorderCard.ViewModel>() {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.pk_activity_reorder_card
    override val viewModel: IReorderCard.ViewModel by viewModels<ReorderCardVM>()
    override val navigationGraphId: Int = R.navigation.pk_reorder_card_navigation
    override val navigationGraphStartDestination: Int
        get() = intent?.getIntExtra(NAVIGATION_GRAPH_START_DESTINATION_ID, 0) ?: 0

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        // To be handle later
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.card = intent?.getParcelableExtra(ReorderCardActivity::class.java.name)
    }

    override fun onClick(id: Int) = Unit
}
