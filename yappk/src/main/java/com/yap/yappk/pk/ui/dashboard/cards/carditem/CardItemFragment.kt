package com.yap.yappk.pk.ui.dashboard.cards.carditem

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseFragment
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.extensions.visible
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.extensions.visiable
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardItemBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.pk.ui.dashboard.cards.carditem.bottomsheetadapter.CardDetailQuickViewAdapter
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardStatus
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapterListener
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardItemFragment(
    var card: Card,
    val position: Int = -1,
    val callback: CardPagerAdapterListener? = null
) :
    BaseFragment<FragmentCardItemBinding, ICardItem.State, ICardItem.ViewModel>(R.layout.fragment_card_item),
    ICardItem.View {

    lateinit var adapter: CardDetailQuickViewAdapter

    override fun getBindingVariable(): Int = BR.viewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override val viewModel: ICardItem.ViewModel by viewModels<CardItemVM>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notifyChangeFragment()
    }

    private fun setCardState() {
        viewModel.getCardState(
            card,
            viewModel.sessionManager.userAccount.value
        )
    }

    private fun setContentOnCard() {
        mViewBinding.tvCardBalance.text = viewModel.getCardBalance(card)
        mViewBinding.ivCard.loadImage(
            card.frontImage,
            ContextCompat.getDrawable(requireContext(), card.getCardImageResource()),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.flowSeeDetail, R.id.ivCard -> {
                routeCardDetail(viewModel.cardState.value)
            }
            R.id.tvCardAction -> {
                routeCardAction(viewModel.cardState.value)
            }

            R.id.clCardStatus -> {
                routeCardStatusAction(viewModel.cardState.value)
            }
        }
    }

    private fun routeCardStatusAction(cardState: CardState?) {
        when (cardState) {
            CardState.ACTIVATED -> handleCardActiveStatusAction()
            else -> Unit
        }
    }

    private fun handleCardActiveStatusAction() {
        viewModel.openCardDetailsBottomSheet()
        viewModel.fetchCardDetails(card.cardSerialNumber ?: "")
    }

    private fun routeCardDetail(cardState: CardState?) {
        when (cardState) {
            CardState.SET_PIN_REQUIRED,
            CardState.DELIVERY_IN_PROGRESS,
            CardState.NOT_ORDERED ->
                callback?.openCardStatusScreen(card)
            CardState.ACTIVATED, CardState.FREEZE -> callback?.openCardDetailScreen(card)
            else -> Unit
        }
    }

    private fun routeCardAction(cardState: CardState?) {
        when (cardState) {
            CardState.SET_PIN_REQUIRED -> callback?.openCardPinScreen(card)
            CardState.FREEZE -> viewModel.updateCardFreezeConfig(card.cardSerialNumber ?: "")
            CardState.RE_ORDER_REQUIRED -> callback?.cardReorderFlow(card)
            else -> Unit
        }
    }

    private fun handleCardState(cardState: CardState) {
        when (cardState) {
            CardState.ACTIVATED -> setCardActivatedContent()
            CardState.NOT_ORDERED -> setCardNotOrderContent()
            CardState.DELIVERY_IN_PROGRESS -> setDeliveryInProgressContent()
            CardState.EXPIRED -> setCardExpiredContent()
            CardState.FREEZE -> setCardFreezeContent()
            CardState.SET_PIN_REQUIRED -> setCardPinRequiredContent()
            CardState.RE_ORDER_REQUIRED -> setCardReOrderContent()
        }
    }

    private fun setCardNotOrderContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_exclamation_blue)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_need_kyc)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.invisible()
        mViewBinding.flowSeeDetail.visible()
    }

    private fun setCardActivatedContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_view_purple)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_card_balance)
        mViewBinding.tvCardBalance.visiable()
        mViewBinding.tvCardAction.invisible()
        mViewBinding.flowSeeDetail.visible()
    }

    private fun setDeliveryInProgressContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_time)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_on_the_way)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.invisible()
        mViewBinding.flowSeeDetail.visible()
    }

    private fun setCardExpiredContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_exclamation)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_expire)
        mViewBinding.tvCardAction.text =
            requireContext().getString(screen_card_section_display_text_update_card)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.visible()
        mViewBinding.flowSeeDetail.invisible()
    }

    private fun setCardFreezeContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_lock)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_freeze)
        mViewBinding.tvCardAction.text =
            requireContext().getString(screen_card_section_display_text_unfreeze_card)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.visible()
        mViewBinding.flowSeeDetail.visiable()
    }

    private fun setCardPinRequiredContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_exclamation_blue)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_set_pin)
        mViewBinding.tvCardAction.text =
            requireContext().getString(screen_card_section_display_text_btn_lets)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.visible()
        mViewBinding.flowSeeDetail.visible()
    }

    private fun setCardReOrderContent() {
        mViewBinding.ivCardStatus.setImageResource(R.drawable.pk_ic_exclamation)
        mViewBinding.tvDescription.text =
            requireContext().getString(screen_card_section_display_text_blocked)
        mViewBinding.tvCardAction.text =
            requireContext().getString(screen_card_section_display_text_re_order)
        mViewBinding.tvCardBalance.invisible()
        mViewBinding.tvCardAction.visible()
        mViewBinding.flowSeeDetail.invisible()
    }

    private fun openCardDetailsQuickView(navigate: SingleEvent<Boolean>) {
        navigate.getContentIfNotHandled()?.let {
            if (it) {
                getBottomSheetBuilder()?.show(childFragmentManager, "")
            }
        }
    }

    private fun getBottomSheetBuilder(): BottomSheetUIDialog? {
        adapter = CardDetailQuickViewAdapter(
            arrayListOf(
                CardDetail(
                    state = StateEnum.LOADING,
                    title = viewModel.getCardName(card)
                )
            ),
            onItemReload
        )
        val builder =
            BottomSheetBuilder().setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        return builder.build()
    }

    private fun handleCardDetail(cardDetails: CardDetail) {
        cardDetails.title = viewModel.getCardName(card)
        adapter.setList(arrayListOf(cardDetails))
    }

    private val onItemReload = object : MultiStateLayout.OnReloadListener {
        override fun onReload(view: View) {
            handleCardDetail(
                CardDetail(
                    state = StateEnum.LOADING,
                    title = viewModel.getCardName(card)
                )
            )
            viewModel.fetchCardDetails(cardSerialNumber = card.cardSerialNumber ?: "")
        }
    }

    private fun handleUpdateFreezeCardConfig(isFreezeCardConfigUpdated: Boolean) {
        if (isFreezeCardConfigUpdated) {
            if (card.status == CardStatus.BLOCKED.name) {
                card.status = CardStatus.ACTIVE.name
            } else if (card.status == CardStatus.ACTIVE.name) {
                card.status = CardStatus.BLOCKED.name
            }
            callback?.updateCardPagerItem(card, position)
        }
    }

    override fun notifyChangeFragment() {
        setCardState()
        setContentOnCard()
    }

    override fun viewModelObservers() {
        observe(viewModel.cardState, ::handleCardState)
        observe(viewModel.cardDetails, ::handleCardDetail)
        observe(viewModel.isFreezeCardConfigUpdated, ::handleUpdateFreezeCardConfig)
        observeEvent(viewModel.openCardDetailsBottomSheet, ::openCardDetailsQuickView)

    }

}