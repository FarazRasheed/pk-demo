package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentChangeCardPinSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeCardPinSuccessFragment :
    BaseNavFragment<PkFragmentChangeCardPinSuccessBinding, IChangeCardPinSuccess.State, IChangeCardPinSuccess.ViewModel>(
        R.layout.pk_fragment_change_card_pin_success
    ) {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IChangeCardPinSuccess.ViewModel by viewModels<ChangeCardPinSuccessVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnDone -> {
                navigateWithPopup(R.id.primaryCardDetailFragment, R.id.primaryCardDetailFragment)
            }
        }
    }
}
