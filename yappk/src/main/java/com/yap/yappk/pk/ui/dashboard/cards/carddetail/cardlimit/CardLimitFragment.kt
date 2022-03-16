package com.yap.yappk.pk.ui.dashboard.cards.carddetail.cardlimit

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardLimitBinding
import com.yap.yappk.pk.ui.dashboard.cards.CardsFragment
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardLimitFragment :
    BaseNavFragment<FragmentCardLimitBinding, ICardLimit.State, ICardLimit.ViewModel>(
        R.layout.fragment_card_limit
    ),
    ICardLimit.View, ToolBarView.OnToolBarViewClickListener,
    CompoundButton.OnCheckedChangeListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardLimit.ViewModel by viewModels<CardLimitVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.swAtm.setOnCheckedChangeListener(this)
        mViewBinding.swRetail.setOnCheckedChangeListener(this)
        setValues()
    }

    private fun setValues() {
        mViewBinding.swAtm.isChecked = parentViewModel.card?.atmAllowed ?: false
        mViewBinding.swRetail.isChecked = parentViewModel.card?.retailPaymentAllowed ?: false
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun onClick(id: Int) = Unit


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView?.isPressed == true)
            when (buttonView.id) {
                R.id.swAtm -> viewModel.updateAtmConfig(
                    parentViewModel.card?.cardSerialNumber ?: ""
                )
                R.id.swRetail -> viewModel.updateRetailConfig(
                    parentViewModel.card?.cardSerialNumber ?: ""
                )
            }
    }

    private fun handleUpdateAtmConfig(isAtmConfigUpdated: Boolean) {
        if (isAtmConfigUpdated) {
            parentViewModel.card?.atmAllowed = !(parentViewModel.card?.atmAllowed ?: false)
            setBackData()
        } else {
            mViewBinding.swAtm.isChecked = parentViewModel.card?.atmAllowed ?: false
        }
    }

    private fun handleUpdateRetailConfig(isRetailConfigUpdated: Boolean) {
        if (isRetailConfigUpdated) {
            parentViewModel.card?.retailPaymentAllowed =
                !(parentViewModel.card?.retailPaymentAllowed ?: false)
            setBackData()
        } else {
            mViewBinding.swRetail.isChecked = parentViewModel.card?.retailPaymentAllowed ?: false
        }
    }

    private fun setBackData() {
        val intent = requireActivity().intent
        intent.putExtra(
            CardsFragment::class.java.name, parentViewModel.card
        )
        intent.putExtra("cardPosition", parentViewModel.cardPosition)
        requireActivity().setResult(Activity.RESULT_OK, intent)
    }

    override fun viewModelObservers() {
        observe(viewModel.isAtmConfigUpdated, ::handleUpdateAtmConfig)
        observe(viewModel.isRetailConfigUpdated, ::handleUpdateRetailConfig)
    }

}
