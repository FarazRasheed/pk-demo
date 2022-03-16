package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinintro

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardPinIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardPinIntroFragment :
    BaseNavFragment<FragmentCardPinIntroBinding, ICardPinIntro.State, ICardPinIntro.ViewModel>(R.layout.fragment_card_pin_intro),
    ICardPinIntro.View {
    override fun getBindingVariable(): Int = BR.viewModel
    val viewModels = viewModels<CardPinIntroVM>()
    override val viewModel: ICardPinIntro.ViewModel by viewModels

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnCreate -> {
                navigate(R.id.action_cardPinIntroFragment_to_cardPinEnterFragment)
            }
            R.id.btnDoItLater -> {
                requireActivity().onBackPressed()
            }
        }
    }
}