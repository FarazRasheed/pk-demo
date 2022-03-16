package com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.ActivityCardDeliveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDeliveryActivity :
    BaseNavActivity<ActivityCardDeliveryBinding, ICardDelivery.State, ICardDelivery.ViewModel>(),
    ICardDelivery.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_card_delivery
    override val viewModel: ICardDelivery.ViewModel by viewModels<CardDeliveryVM>()
    override val navigationGraphId: Int = R.navigation.pk_card_status_nav_graph

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        // To be handle later
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.debitCard = intent?.getParcelableExtra(CardDeliveryActivity::class.java.name)
    }
}