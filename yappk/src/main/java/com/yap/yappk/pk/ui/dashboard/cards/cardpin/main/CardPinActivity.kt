package com.yap.yappk.pk.ui.dashboard.cards.cardpin.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.digitify.core.base.BaseNavActivity
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.ActivityCardPinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardPinActivity :
    BaseNavActivity<ActivityCardPinBinding, ICardPin.State, ICardPin.ViewModel>(),
    ICardPin.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_card_pin
    override val viewModel: ICardPin.ViewModel by viewModels<CardPinVM>()
    override val navigationGraphId: Int = R.navigation.pk_card_pin_nav_graph

    override fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    ) {
        // To be handle later
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.debitCard = intent?.getParcelableExtra(CardPinActivity::class.java.name)
    }
}