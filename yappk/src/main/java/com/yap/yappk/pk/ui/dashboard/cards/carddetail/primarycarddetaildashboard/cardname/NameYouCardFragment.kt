package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardname

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentNameYourCardBinding
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import com.yap.yappk.pk.utils.UPDATE_CARD_NAME_RESULT_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameYouCardFragment :
    BaseNavFragment<PkFragmentNameYourCardBinding, INameYouCard.State, INameYouCard.ViewModel>(
        R.layout.pk_fragment_name_your_card
    ), INameYouCard.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: INameYouCard.ViewModel by viewModels<NameYouCardVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        if (!parentViewModel.card?.cardName.isNullOrBlank()){
            mViewBinding.etCardName.setText(parentViewModel.card?.cardName)
        }
        mViewBinding.ivCard.loadImage(
            parentViewModel.card?.frontImage,
            ContextCompat.getDrawable(
                requireContext(),
                parentViewModel.card?.getCardImageResource() ?: R.drawable.pk_card_spare
            ),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnConfirm -> {
                viewModel.setCardNameRequest(
                    mViewBinding.etCardName.text.toString(),
                    parentViewModel.card?.cardSerialNumber ?: ""
                )
            }
        }
    }

    private fun handleSetCardNameSuccess(success: Boolean) {
        if (success) viewModel.navigateToCardDashboard()
    }

    private fun navigateToCardDashboard(navigateEvent: SingleEvent<Int>) {
        navigateEvent.getContentIfNotHandled()?.let {
            navigateBackWithResult(
                UPDATE_CARD_NAME_RESULT_CODE,
                bundleOf(NameYouCardFragment::class.java.name to mViewBinding.etCardName.text.toString())
            )
        }
    }

    override fun onStartIconClicked() {
        super.onStartIconClicked()
        requireActivity().onBackPressed()
    }

    override fun viewModelObservers() {
        observe(viewModel.setCardNameSuccess, ::handleSetCardNameSuccess)
        observeEvent(viewModel.navigateToCardDashboard, ::navigateToCardDashboard)
    }
}