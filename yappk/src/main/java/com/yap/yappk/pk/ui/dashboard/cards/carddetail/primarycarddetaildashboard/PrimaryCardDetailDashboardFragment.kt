package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.extensions.visible
import com.digitify.core.utils.SingleEvent
import com.google.android.material.snackbar.Snackbar
import com.yap.uikit.utils.spannables.underline
import com.yap.uikit.widget.bottomsheet.BottomSheetBuilder
import com.yap.uikit.widget.bottomsheet.BottomSheetUIDialog
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.multiStateView.Status
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardDetailBinding
import com.yap.yappk.databinding.LayoutCardDetailDialogViewBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_card_detail_display_text_frozen
import com.yap.yappk.localization.screen_card_detail_display_text_unfreeze_now
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.ui.dashboard.cards.CardsFragment
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.bottomsheetadapter.PrimaryCardDetailMoreViewAdapter
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.carddetaildialog.CardDetailDialogVH
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardname.NameYouCardFragment
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionfilter.CardTransactionFilterFragment
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardSettings
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardStatus
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.utils.*
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrimaryCardDetailDashboardFragment :
    BaseNavFragment<FragmentCardDetailBinding, IPrimaryCardDetailDashboard.State, IPrimaryCardDetailDashboard.ViewModel>(
        R.layout.fragment_card_detail
    ),
    IPrimaryCardDetailDashboard.View, ToolBarView.OnToolBarViewClickListener,
    BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IPrimaryCardDetailDashboard.ViewModel by viewModels<PrimaryCardDetailDashboardVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    lateinit var adapter: PrimaryCardDetailMoreViewAdapter
    var moreBottomDialog: BottomSheetUIDialog? = null
    var mCardDetailAlertDialog: AlertDialog? = null
    var mCardDetailDialogVH: BaseViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCardValues()
        moreBottomDialog = getBottomSheetBuilder()
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setupListAdaptor()
    }

    private fun setCardValues() {
        viewModel.mCardSerialNumber = parentViewModel.card?.cardSerialNumber
        setFreezeCardValues()
        viewModel.viewState.cardName.value =
            viewModel.getCardName(parentViewModel.card ?: Card())
        viewModel.viewState.cardType.value =
            viewModel.getCardTypeText(parentViewModel.card ?: Card())
        viewModel.viewState.cardBalance.value =
            viewModel.getCardBalance(parentViewModel.card ?: Card())
        viewModel.viewState.cardMaskNo.value =
            parentViewModel.card?.maskedCardNo
        mViewBinding.ivCard.loadImage(
            parentViewModel.card?.frontImage,
            ContextCompat.getDrawable(
                requireContext(),
                parentViewModel.card?.getCardImageResource() ?: R.drawable.pk_card_spare
            ),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    private fun setFreezeCardValues() {
        viewModel.viewState.isCardFreezes.value =
            parentViewModel.card?.status == CardStatus.BLOCKED.name
        viewModel.viewState.cardFreezeText.value =
            viewModel.getCardFreezeText(viewModel.viewState.isCardFreezes.value ?: false)
        if (viewModel.viewState.isCardFreezes.value == true) {
            showCardFreezeSnackBar()
        }
    }

    private fun showCardFreezeSnackBar() {
        val actionText = requireContext().getString(screen_card_detail_display_text_unfreeze_now)
        mViewBinding.clSnackbar.showCustomViewSnackBar(
            msg = requireContext().getString(screen_card_detail_display_text_frozen),
            viewBgColor = R.color.pkBlueWithAHintOfPurple,
            layoutId = R.layout.layout_snackbar_card_view,
            messageTextViewId = R.id.tvMessage,
            actionTextViewId = R.id.tvAction,
            actionText = underline(actionText),
            messageTextStyle = R.style.Yap_Pk_Widget_TextView_WhiteColor_XSmall,
            actionTextStyle = R.style.Yap_Pk_Widget_TextView_WhiteColor_XSmall,
            duration = Snackbar.LENGTH_INDEFINITE,
            gravity = Gravity.TOP,
            clickListener = snackActionListener
        )
    }

    private val snackActionListener = View.OnClickListener {
        viewModel.updateCardFreezeConfig(viewModel.mCardSerialNumber ?: "")
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvCardDetails -> {
                viewModel.fetchCardDetails(viewModel.mCardSerialNumber ?: "")
                openCardDetailDialog()
            }
            R.id.fabCardFreeze -> viewModel.updateCardFreezeConfig(
                viewModel.mCardSerialNumber ?: ""
            )
            R.id.fabLimit -> navigate(R.id.action_primaryCardDetailFragment_to_cardLimitFragment)
            R.id.tvFilter -> navigateForResult(
                resId = R.id.action_primaryCardDetailFragment_to_cardTransactionFilterFragment,
                requestCode = CARD_FILTER_TRANSACTION_REQUEST_CODE,
                bundleOf(CardTransactionFilterFragment::class.java.name to viewModel.filterData)
            )
        }
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun onEndIconClicked() {
        viewModel.openCardDetailMoreBottomSheet()
    }

    private fun getBottomSheetBuilder(): BottomSheetUIDialog? {
        adapter =
            PrimaryCardDetailMoreViewAdapter(viewModel.getMoreOptionList(), rvMoreItemClickListener)
        val builder =
            BottomSheetBuilder().setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        return builder.build()
    }

    private fun openCardDetailMoreView(navigate: SingleEvent<Boolean>) {
        navigate.getContentIfNotHandled()?.let {
            if (it) {
                moreBottomDialog?.show(childFragmentManager, "")
            }
        }
    }

    private val rvMoreItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is String) {
                moreBottomDialog?.dismiss()
                routeMoreOption(
                    cardSettings = viewModel.getTargetScreen(
                        position = pos,
                        card = parentViewModel.card
                    )
                )
            }
        }
    }

    private fun openCardDetailDialog() {
        mCardDetailDialogVH =
            CardDetailDialogVH(CardDetail(),parentViewModel.card, cardDialogViewClickListener, onItemReload)
        mCardDetailDialogVH?.let { dialogVH ->
            mCardDetailAlertDialog = requireActivity().showDialog(dialogVH, true) {
                LayoutCardDetailDialogViewBinding.inflate(layoutInflater)
            }
        }
    }

    private fun updateCardDetailVH(cardDetail: CardDetail?) {
        cardDetail?.let { detail ->
            mCardDetailDialogVH?.notifyDatasetRefresh(detail)
        }
    }

    private val cardDialogViewClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.ivClose -> mCardDetailAlertDialog?.dismiss()
            }
        }
    }

    private fun handleCardDetail(cardDetails: CardDetail) {
        cardDetails.title = viewModel.viewState.cardName.value ?: ""
        cardDetails.cardType = viewModel.getCardTypeText(parentViewModel.card ?: Card())
        updateCardDetailVH(cardDetails)

    }

    private fun routeMoreOption(cardSettings: CardSettings) {
        when (cardSettings) {
            is CardSettings.ChangeCardName -> navigateForResult(
                R.id.action_primaryCardDetailFragment_to_nameYouCardFragment,
                UPDATE_CARD_NAME_RESULT_CODE
            )
            is CardSettings.ChangeCardPin -> navigate(R.id.action_primaryCardDetailFragment_to_pk_change_card_pin_nav_graph)
            is CardSettings.ForgotCardPin -> navigate(R.id.action_primaryCardDetailFragment_to_pk_forgot_card_pin_nav_graph)
            is CardSettings.ReportOrStolenCard -> navigate(R.id.action_primaryCardDetailFragment_to_reportCardFragment)
            is CardSettings.ViewCardStatement -> Unit
            CardSettings.None -> Unit
        }
    }

    private fun handleUpdateFreezeCardConfig(isFreezeCardConfigUpdated: Boolean) {
        if (isFreezeCardConfigUpdated) {
            if (parentViewModel.card?.status == CardStatus.BLOCKED.name) {
                parentViewModel.card?.status = CardStatus.ACTIVE.name
                cancelAllSnackBar()
            } else if (parentViewModel.card?.status == CardStatus.ACTIVE.name) {
                parentViewModel.card?.status = CardStatus.BLOCKED.name
            }
            setBackData()
            setFreezeCardValues()
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

    private val onItemReload = object : MultiStateLayout.OnReloadListener {
        override fun onReload(view: View) {
            handleCardDetail(CardDetail(state = StateEnum.LOADING))
            viewModel.fetchCardDetails(parentViewModel.card?.cardSerialNumber ?: "")
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == UPDATE_CARD_NAME_RESULT_CODE) {
            parentViewModel.card.also { card ->
                card?.nameUpdated = true
                card?.cardName = result.data?.getString(NameYouCardFragment::class.java.name)
            }
            setCardValues()
            setBackData()
        } else if (result.requestCode == CARD_FILTER_TRANSACTION_REQUEST_CODE
            && result.resultCode == Activity.RESULT_OK
        ) {
            viewModel.filterData =
                result.data?.getParcelable(CardTransactionFilterFragment::class.java.name)
            setFilterView()
            mViewBinding.rvCardsTransactions.pagination?.notifyPaginationRestart()
        }
    }

    private fun setFilterView() {
        if (viewModel.filterData?.isFilterSet == true) {
            mViewBinding.ivFilter.gone()
            mViewBinding.tvFilterCount.text = viewModel.filterData?.filterCount.toString()
            mViewBinding.tvFilterCount.visible()
        } else {
            mViewBinding.ivFilter.visible()
            mViewBinding.tvFilterCount.gone()
        }
    }

    private fun setupListAdaptor() {
        mViewBinding.multiStateView.setOnReloadListener(viewModel.getReloadListener())
        viewModel.cardTransactionListAdapter.allowFullItemClickListener = true
        viewModel.cardTransactionListAdapter.onItemClickListener = rvItemClickListener
        mViewBinding.rvCardsTransactions.apply {
            adapter = viewModel.cardTransactionListAdapter
            threshold = 3
            pagination = viewModel.getPaginationListener()
        }
//        mViewBinding.rvCardsTransactions.adapter = viewModel.cardTransactionListAdapter
//        mViewBinding.rvCardsTransactions.threshold = 3
//        mViewBinding.rvCardsTransactions.pagination = viewModel.getPaginationListener()
        if (viewModel.isFirstTimeTransactionFetching)
            mViewBinding.rvCardsTransactions.pagination?.notifyPaginationRestart()

    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Transaction) Unit
        }
    }

    private fun handleCardTransactions(cardTransactions: List<Transaction>) =
        viewModel.sortByDate(
            pageNumber = viewModel.mPageNumber,
            viewModel.cardTotalTransaction.value ?: listOf(),
            cardTransactions
        )


    private fun handleTotalCardTransactions(cardTransactions: List<Transaction>) =
        viewModel.cardTransactionListAdapter.updateCardListItems(cardTransactions)


    private fun handleTransactionSuccess(isSuccess: Boolean) {
        if (isSuccess) viewModel.getPaginationListener().notifyPageLoaded() else
            viewModel.getPaginationListener().notifyPageError()
    }

    private fun handleTransactionCompleted(isCompleted: Boolean) {
        if (isCompleted) viewModel.getPaginationListener().notifyPaginationCompleted()
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

    override fun viewModelObservers() {
        observeEvent(viewModel.openCardDetailMoreBottomSheet, ::openCardDetailMoreView)
        observe(viewModel.isFreezeCardConfigUpdated, ::handleUpdateFreezeCardConfig)
        observe(viewModel.cardDetails, ::handleCardDetail)
        observe(viewModel.cardTransaction, ::handleCardTransactions)
        observe(viewModel.cardTotalTransaction, ::handleTotalCardTransactions)
        observe(viewModel.isTransactionsSuccess, ::handleTransactionSuccess)
        observe(viewModel.isTransactionsCompleted, ::handleTransactionCompleted)
        observe(viewModel.multiStateView, ::handleViewState)
    }
}