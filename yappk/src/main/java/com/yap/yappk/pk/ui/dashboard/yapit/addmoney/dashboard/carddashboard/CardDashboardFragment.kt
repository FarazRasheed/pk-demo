package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.extensions.visible
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardDashboardBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarddetail.ExternalCardDetailFragment
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerListener
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.transfer.TopUpViaExternalCardFragment
import com.yap.yappk.pk.utils.ADD_EXTERNAL_CARD_REQUEST_CODE
import com.yap.yappk.pk.utils.EXTERNAL_CARD_DETAIL_REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class CardDashboardFragment :
    BaseNavFragment<FragmentCardDashboardBinding, ICardDashboard.State, ICardDashboard.ViewModel>(
        R.layout.fragment_card_dashboard
    ), ICardDashboard.View, ToolBarView.OnToolBarViewClickListener, ExternalCardPagerListener,
    BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ICardDashboard.ViewModel by viewModels<CardDashboardVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        handleCardApiCall()
    }

    private fun handleCardApiCall() {
        if (viewModel.externalCards.value != null)
            handleExternalCards(viewModel.externalCards.value ?: listOf())
        else
            viewModel.getExternalCards()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnSelect -> openExternalCardTopUp(viewModel.card.value?.peekContent())
        }
    }

    private fun setupPagerAdaptor(cardsList: List<ExternalCard>) {
        viewModel.cardsAdapter = ExternalCardPagerAdapter(
            requireActivity(),
            cardsList as ArrayList<ExternalCard>,
            this
        )
        mViewBinding.viewPagerCard.adapter = viewModel.cardsAdapter
        // You need to retain one page on each side so that the next and previous items are visible
        mViewBinding.viewPagerCard.offscreenPageLimit = 1
        mViewBinding.viewPagerCard.setPageTransformer(getPagerTransformer())
        handleViewPagerCallback()
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    private fun handleExternalCards(list: List<ExternalCard>) {
        setValues(list.size)
        setupPagerAdaptor(list)
    }

    private fun handleCardOnFront(cardOnFront: SingleEvent<ExternalCard?>) {
        cardOnFront.getContentIfNotHandled()?.let { card ->
            setViews(card.alias ?: "", true)
        } ?: setViews("", false)
    }

    private fun setViews(cardName: String, isButtonVisible: Boolean) {
        mViewBinding.tvCardName.text = cardName
        if (isButtonVisible) mViewBinding.btnSelect.visible() else
            mViewBinding.btnSelect.invisible()
    }

    private fun handleViewPagerCallback() {
        mViewBinding.viewPagerCard.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCard(card = viewModel.cardsAdapter?.getCard(position))
                viewModel.pagerCurrentPosition = position
            }
        })
    }


    private fun getPagerTransformer(): ViewPager2.PageTransformer {
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.pk_margin_xl)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.pk_margin_xl)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        return ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.15f * abs(position))
            // If you want a fading effect uncomment the next line:
            page.alpha = 0.25f + (1 - abs(position))
        }
    }

    override fun onEndIconClicked() {
        openAddCardScreen()
    }

    override fun openAddCardScreen() {
        navigateForResult(
            resId = R.id.action_cardDashboardFragment_to_addExternalCardFragment,
            requestCode = ADD_EXTERNAL_CARD_REQUEST_CODE
        )
    }

    override fun openCardDetailScreen() {
        navigateForResult(
            requestCode = EXTERNAL_CARD_DETAIL_REQUEST_CODE,
            resId = R.id.action_cardDashboardFragment_to_externalCardDetailFragment,
            args = bundleOf(
                ExternalCardDetailFragment::class.java.name to viewModel.card.value?.peekContent()
            )
        )
    }

    override fun openExternalCardTopUp(externalCard: ExternalCard?) {
        externalCard?.let {
            navigate(
                R.id.action_cardDashboardFragment_to_externalCardTopUpFragment, bundleOf(
                    TopUpViaExternalCardFragment::class.java.name to externalCard
                )
            )
        }
    }

    private fun setValues(size: Int) {
        if (size == 0) {
            mViewBinding.tbView.tbTitleText = requireContext().getString(
                screen_add_money_card_dashboard_display_text_no_card_tb_title
            )
            mViewBinding.tvTitle.text = requireContext().getString(
                screen_add_money_card_dashboard_display_text_no_card_title
            )
            mViewBinding.tvDescription.text = requireContext().getString(
                screen_add_money_card_dashboard_display_text_no_card_description
            )
        } else {
            mViewBinding.tbView.tbTitleText = requireContext().getString(
                screen_add_money_card_dashboard_display_text_card_tb_title
            )
            mViewBinding.tvTitle.text = requireContext().getString(
                screen_add_money_card_dashboard_display_text_card_title,
                size.toString()
            )
            mViewBinding.tvDescription.text = requireContext().getString(
                screen_add_money_card_dashboard_display_text_card_description
            )
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        when {
            result.requestCode == EXTERNAL_CARD_DETAIL_REQUEST_CODE
                    && result.resultCode == Activity.RESULT_OK -> {
                viewModel.getExternalCards()
            }
            result.requestCode == ADD_EXTERNAL_CARD_REQUEST_CODE
                    && result.resultCode == Activity.RESULT_OK -> {
                result.data?.getParcelable<ExternalCard>(ExternalCard::class.java.name)?.let {
                    if (it.isTopUp) openExternalCardTopUp(it)
                }
                viewModel.getExternalCards()
            }
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.externalCards, ::handleExternalCards)
        observeEvent(viewModel.card, ::handleCardOnFront)
    }
}