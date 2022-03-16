package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcarditem

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentAddExternalCardItemBinding
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerListener
import com.yap.yappk.pk.utils.ItemCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExternalCardItemFragment(
    val position: Int? = null,
    val callback: ExternalCardPagerListener? = null
) :
    BaseFragment<FragmentAddExternalCardItemBinding, IAddExternalCardItem.State, IAddExternalCardItem.ViewModel>(
        R.layout.fragment_add_external_card_item
    ),
    IAddExternalCardItem.View, ItemCard.OnCardItemClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IAddExternalCardItem.ViewModel by viewModels<AddExternalCardItemVM>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.icAddCard.setOnItemCardClickListener(this)
    }

    override fun onClick(id: Int) {
        when (id) {
            //handle later
        }
    }

    override fun onItemClicked(view: View) {
        when (view.id) {
            R.id.icAddCard -> {
                callback?.openAddCardScreen()
            }
        }
    }
}