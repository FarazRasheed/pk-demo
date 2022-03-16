package com.yap.yappk.pk.ui.dashboard.cards.carddetail.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.ActivityCardDetailMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDetailMainActivity :
    BaseNavActivity<ActivityCardDetailMainBinding, ICardDetailMain.State, ICardDetailMain.ViewModel>(),
    ICardDetailMain.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_card_detail_main
    override val viewModel: ICardDetailMain.ViewModel by viewModels<CardDetailMainVM>()
    override val navigationGraphId: Int = R.navigation.pk_card_detail_nav_graph
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
        viewModel.card = intent?.getParcelableExtra(CardDetailMainActivity::class.java.name)
        viewModel.cardPosition = intent?.getIntExtra("cardPosition",-1)
    }
}