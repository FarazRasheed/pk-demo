package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinsuccess

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finish
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardPinSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardPinSuccessFragment :
    BaseNavFragment<FragmentCardPinSuccessBinding, ICardPinSuccess.State, ICardPinSuccess.ViewModel>(
        R.layout.fragment_card_pin_success
    ),
    ICardPinSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel
    val viewModels = viewModels<CardPinSuccessVM>()
    override val viewModel: ICardPinSuccess.ViewModel by viewModels

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnTopUp -> {
                showToast("Under Development")
            }
            R.id.btnDoItLater -> {
                requireActivity().setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}