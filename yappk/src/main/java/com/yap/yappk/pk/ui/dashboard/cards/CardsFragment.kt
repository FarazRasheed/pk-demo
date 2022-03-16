package com.yap.yappk.pk.ui.dashboard.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.multiStateView.Status
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardsBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_card_detail_display_text_inactive
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainActivity
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.main.CardPinActivity
import com.yap.yappk.pk.ui.dashboard.cards.cardstatus.main.CardDeliveryActivity
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardState
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapter
import com.yap.yappk.pk.ui.dashboard.cards.pageradapter.CardPagerAdapterListener
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.ReorderCardActivity
import com.yap.yappk.pk.ui.dashboard.main.DashboardVM
import com.yap.yappk.pk.ui.dashboard.main.IDashboard
import com.yap.yappk.pk.utils.enums.PKPartnerBankStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class CardsFragment :
    BaseNavFragment<FragmentCardsBinding, ICards.State, ICards.ViewModel>(R.layout.fragment_cards),
    ICards.View, ToolBarView.OnToolBarViewClickListener, CardPagerAdapterListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICards.ViewModel by viewModels<CardsVM>()
    private val parentViewModel: IDashboard.ViewModel by activityViewModels<DashboardVM>()

    override fun onClick(id: Int) {
        /*when (id) {
            // handle later
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAccountValues()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        // parentViewModel.fetchCards()
    }

    private fun setAccountValues() {
        if (PKPartnerBankStatus.SIGN_UP_PENDING.name != viewModel.sessionManager.userAccount.value?.partnerBankStatus
            && PKPartnerBankStatus.IBAN_ASSIGNED.name != viewModel.sessionManager.userAccount.value?.partnerBankStatus
        )
            viewModel.viewState.isAccountCreated.value = true
    }

    private fun handleUserCardsList(cardsList: MutableList<Card>) {
        viewModel.setCardList(cardsList)
    }

    private fun handleCardList(list: List<Card>) {
        setupPagerAdaptor(list)
        setupListAdaptor(viewModel.sortByType(list))
    }

    private fun setupListAdaptor(cardList: List<Card>) {
        mViewBinding.multiStateView.setOnReloadListener(object : MultiStateLayout.OnReloadListener {
            override fun onReload(view: View) {
                parentViewModel.fetchCards()
            }
        })
        viewModel.cardListAdapter.allowFullItemClickListener = true
        viewModel.cardListAdapter.onItemClickListener = rvItemClickListener
        viewModel.cardListAdapter.updateCardListItems(cardList)
        viewModel.onCardListCount(viewModel.cardListAdapter.itemCount)
        mViewBinding.rvCards.adapter = viewModel.cardListAdapter
    }

    private fun handleCardOnFront(cardOnFront: SingleEvent<Card?>) {
        cardOnFront.getContentIfNotHandled()?.let { card ->
            val cardName = viewModel.getCardName(card)
            setCardName(cardName)
        } ?: setCardName("")
    }

    private fun setCardName(cardName: String) {
        mViewBinding.tvCardName.text = cardName
    }

    override fun onStartIconClicked() {
        viewModel.viewState.isListView.value = !(viewModel.viewState.isListView.value ?: false)
    }

    override fun onEndIconClicked() {
        openAddCardScreen()
    }

    private fun handleViewPagerCallback() {
        mViewBinding.viewPagerCard.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCard(card = viewModel.cardsAdapter.getCard(position))
                mViewBinding.tvCardsCount.visibility =
                    if (viewModel.cardsAdapter.isLastItem(position)) View.INVISIBLE else View.VISIBLE
                viewModel.pagerCurrentPosition = position
                mViewBinding.tvCardsCount.text =
                    viewModel.getPageCountTitle(
                        cardPosition = position,
                        totalNoOfCards = viewModel.cardsAdapter.getSize()
                    )
            }
        })
    }

    private fun getPagerTransformer(): ViewPager2.PageTransformer {
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.pk_btn_bottom_margin)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.pk_margin_small)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        return ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.10f * abs(position))
            // If you want a fading effect uncomment the next line:
//            page.alpha = 0.25f + (1 - abs(position))
        }
    }

    private fun setupPagerAdaptor(cardsList: List<Card>) {
        viewModel.cardsAdapter = CardPagerAdapter(
            requireActivity(),
            cardsList as ArrayList<Card>,
            this,
            viewModel.viewState.isAccountCreated.value ?: false
        )
        mViewBinding.viewPagerCard.adapter = viewModel.cardsAdapter
        // You need to retain one page on each side so that the next and previous items are visible
        mViewBinding.viewPagerCard.offscreenPageLimit = 1
        mViewBinding.viewPagerCard.setPageTransformer(getPagerTransformer())
        handleViewPagerCallback()
    }

    override fun openAddCardScreen() {
        showToast("Under development")
    }

    override fun openCardDetailScreen(card: Card?) {
        val intent = Intent(requireContext(), CardDetailMainActivity::class.java)
        intent.putExtra(CardDetailMainActivity::class.java.name, card)
        intent.putExtra("cardPosition", viewModel.pagerCurrentPosition)
        intent.putExtra(
            NAVIGATION_GRAPH_START_DESTINATION_ID,
            if (card?.cardType == CardType.DEBIT.name) R.id.primaryCardDetailFragment else R.id.primaryCardDetailFragment
        )
        launchIntent(intent)
    }

    override fun openCardStatusScreen(debitCard: Card?) {
        val intent = Intent(requireContext(), CardDeliveryActivity::class.java)
        intent.putExtra(CardDeliveryActivity::class.java.name, debitCard)
        launchIntent(intent)
    }

    override fun openCardPinScreen(debitCard: Card?) {
        val intent = Intent(requireContext(), CardPinActivity::class.java)
        intent.putExtra(CardPinActivity::class.java.name, debitCard)
        launchIntent(intent)
    }

    private fun startReorderFlow(card: Card?) {
        val intent = Intent(requireContext(), ReorderCardActivity::class.java)
        intent.putExtra(ReorderCardActivity::class.java.name, card)
        launchIntent(intent)
    }

    override fun updateCardPagerItem(card: Card?, position: Int) {
        card?.let {
            viewModel.cardsAdapter.updateData(card, position)
        }
    }

    override fun cardReorderFlow(card: Card?) {
        startReorderFlow(card)
    }

    private fun launchIntent(intent: Intent) {
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshCards()
            }
        }
    }

    private fun refreshCards() {
        parentViewModel.showLoading()
        parentViewModel.fetchCards()
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Card) {
                viewModel.getCardState(
                    card = data,
                    userAccount = viewModel.sessionManager.userAccount.value
                ) { cardState ->
                    handleCardState(cardState = cardState, card = data)
                }
            }
        }
    }

    private fun handleViewState(state: MultiState) {
        when (state.status) {
            Status.LOADING -> mViewBinding.multiStateView.viewState = StateEnum.LOADING
            Status.EMPTY -> mViewBinding.multiStateView.viewState = StateEnum.EMPTY
            Status.ERROR -> mViewBinding.multiStateView.viewState = StateEnum.ERROR
            Status.SUCCESS -> mViewBinding.multiStateView.viewState = StateEnum.CONTENT
            else -> mViewBinding.multiStateView.viewState = StateEnum.CONTENT
        }
    }


    private fun handleCardState(cardState: CardState?, card: Card?) {
        when (cardState) {
            CardState.ACTIVATED, CardState.FREEZE -> openCardDetailScreen(card)
            else -> {
                showToast(requireContext().getString(screen_card_detail_display_text_inactive))
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun viewModelObservers() {
        observe(viewModel.multiStateView, ::handleViewState)
        observe(parentViewModel.userCards, ::handleUserCardsList)
        observe(viewModel.cardList, ::handleCardList)
        observeEvent(viewModel.card, ::handleCardOnFront)
    }
}