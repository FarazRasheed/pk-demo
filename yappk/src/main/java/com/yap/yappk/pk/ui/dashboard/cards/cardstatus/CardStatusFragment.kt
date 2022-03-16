package com.yap.yappk.pk.ui.dashboard.cards.cardstatus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseFragment
import com.digitify.core.extensions.finish
import com.digitify.core.extensions.toggleVisibility
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentCardStatusBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_card_status_display_text_kyc_need
import com.yap.yappk.localization.screen_card_status_display_text_shipped
import com.yap.yappk.localization.screen_card_status_display_text_shipping
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.main.CardPinActivity
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.adapter.CardDeliveryStatusAdapter
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main.CardDeliveryVM
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main.ICardDelivery
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardDeliveryStatus
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardStatusFragment :
    BaseFragment<PkFragmentCardStatusBinding, ICardStatus.State, ICardStatus.ViewModel>(R.layout.pk_fragment_card_status),
    ICardStatus.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    val viewModels = viewModels<CardStatusVM>()
    override val viewModel: ICardStatus.ViewModel by viewModels
    private val parentViewModel: ICardDelivery.ViewModel by activityViewModels<CardDeliveryVM>()

    lateinit var cardStatusAdapter: CardDeliveryStatusAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setCardContent(parentViewModel.debitCard)

    }

    private fun setCardContent(debitCard: Card?) {
        when (debitCard?.deliveryStatus) {
            CardDeliveryStatus.SHIPPING.name, CardDeliveryStatus.ORDERED.name, CardDeliveryStatus.BOOKED.name -> {
                mViewBinding.tvDescription.text =
                    requireContext().getString(screen_card_status_display_text_shipping)
            }
            CardDeliveryStatus.SHIPPED.name -> {
                mViewBinding.tvDescription.text =
                    requireContext().getString(screen_card_status_display_text_shipped)
                viewModel.viewState.valid.value = true
            }
            else -> {
                mViewBinding.tvDescription.text =
                    requireContext().getString(screen_card_status_display_text_kyc_need)
            }
        }
        setCardImage(debitCard)
        setCardStatus(debitCard)
    }

    private fun setCardStatus(debitCard: Card?) {
        val list = when (debitCard?.deliveryStatus) {
            CardDeliveryStatus.ORDERED.name -> viewModel.handleOrderedStatus()
            CardDeliveryStatus.BOOKED.name -> viewModel.handleBookedStatus()
            CardDeliveryStatus.SHIPPING.name -> viewModel.handleShippingStatus()
            CardDeliveryStatus.SHIPPED.name -> viewModel.handleShippedStatus()
            else -> viewModel.handleStatus()
        }
        cardStatusAdapter = CardDeliveryStatusAdapter(list)
        mViewBinding.rcCardStatus.adapter = cardStatusAdapter
    }

    private fun setCardImage(card: Card?) {
        mViewBinding.ivCard.toggleVisibility()
        mViewBinding.ivCard.loadImage(
            card?.frontImage,
            ContextCompat.getDrawable(
                requireContext(),
                card?.getCardImageResource() ?: R.drawable.pk_card_spare
            ),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnActivate -> openCardPinScreen(parentViewModel.debitCard)
        }
    }

    private fun openCardPinScreen(debitCard: Card?) {
        val intent = Intent(requireContext(), CardPinActivity::class.java)
        intent.putExtra(CardPinActivity::class.java.name, debitCard)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                requireActivity().setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }
}