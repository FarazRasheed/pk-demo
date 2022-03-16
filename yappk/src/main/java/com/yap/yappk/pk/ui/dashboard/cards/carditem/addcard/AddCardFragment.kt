package com.yap.yappk.pk.ui.dashboard.cards.carditem.addcard

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAddCardBinding
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapterListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardFragment(
    val position: Int? = null,
    val callback: CardPagerAdapterListener? = null
) :
    BaseFragment<FragmentAddCardBinding, IAddCard.State, IAddCard.ViewModel>(R.layout.fragment_add_card),
    IAddCard.View {
    override fun getBindingVariable(): Int = BR.viewModel
    val viewModels = viewModels<AddCardVM>()
    override val viewModel: IAddCard.ViewModel by viewModels

    override fun onClick(id: Int) {
        when (id) {
            R.id.cvAddCard -> {
                callback?.openAddCardScreen()
            }
        }
    }


}