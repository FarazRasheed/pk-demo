package com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.multiStateView.Status
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardSchemeBenefitsBinding
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_card_scheme_benefits_display_text_free_card_description
import com.yap.yappk.localization.screen_card_scheme_benefits_display_text_paid_card_description
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.cards.responsedtos.CardSchemeBenefit
import com.yap.yappk.pk.ui.kyc.enums.CardSchemeEnum
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSchemeBenefitsFragment :
    BaseNavFragment<FragmentCardSchemeBenefitsBinding, ICardSchemeBenefits.State, ICardSchemeBenefits.ViewModel>(
        R.layout.fragment_card_scheme_benefits
    ), ICardSchemeBenefits.View {

    override fun getBindingVariable(): Int = BR.viewModel
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()
    override val viewModel: ICardSchemeBenefits.ViewModel by viewModels<CardSchemeBenefitsVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        getFragmentArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdaptor()
        setValues(viewModel.cardScheme.value?.peekContent())
    }

    private fun setValues(cardScheme: CardScheme?) {
        mViewBinding.tvCardName.text = cardScheme?.schemeName
        setDescription(
            cardScheme ?: CardScheme(),
            viewModel.sessionManager.userAccount.value?.currency?.code ?: ""
        )
        when (cardScheme?.schemeName) {
            CardSchemeEnum.PayPak.scheme -> {
                mViewBinding.ivCover.setImageResource(R.drawable.paypak_cover)
                mViewBinding.ivCardLogo.setImageResource(R.drawable.paypak_logo)
            }
            else -> {
                mViewBinding.ivCover.setImageResource(R.drawable.mastercard_cover)
                mViewBinding.ivCardLogo.setImageResource(R.drawable.mastercard_logo)
            }
        }
    }

    private fun setDescription(
        data: CardScheme,
        currency: String
    ) {
        val cardFee: Double = data.fee ?: 0.0
        when {
            cardFee > 0 -> {
                val fee = data.fee?.toString()?.toFormattedCurrency(currency = currency) ?: "0"
                mViewBinding.tvCardDescription.text = requireContext().getString(
                    screen_card_scheme_benefits_display_text_paid_card_description,
                    fee
                )
            }
            else -> {
                mViewBinding.tvCardDescription.text = requireContext().getString(
                    screen_card_scheme_benefits_display_text_free_card_description
                )
            }
        }
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setCardScheme(it.getParcelable(CardSchemeBenefit::class.java.name))
        }
    }

    private fun setupListAdaptor() {
        mViewBinding.multiStateView.setOnReloadListener(viewModel.getReloadListener())
        mViewBinding.rvBenefits.apply {
            adapter = viewModel.cardSchemeBenefitAdapter
        }
    }

    private fun handleCardScheme(singleEvent: SingleEvent<CardScheme>) {
        singleEvent.getContentIfNotHandled()?.let { cardScheme ->
            viewModel.getCardSchemeBenefits(cardScheme = cardScheme.schemeName ?: "")
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivClose -> navigateBack()
            R.id.btnSelect -> openCardNameFragment()
        }
    }

    private fun openCardNameFragment() {
        parentViewModel.cardScheme = viewModel.cardScheme.value?.peekContent()
        navigate(
            destinationId = R.id.action_cardSchemeBenefitsFragment_to_cardNameConfirmationFragment
        )
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

    private fun handleCardSchemeBenefits(list: List<CardSchemeBenefit>) {
        viewModel.cardSchemeBenefitAdapter.setList(list)
    }

    override fun viewModelObservers() {
        observeEvent(viewModel.cardScheme, ::handleCardScheme)
        observe(viewModel.cardSchemeBenefits, ::handleCardSchemeBenefits)
        observe(viewModel.multiStateView, ::handleViewState)
    }
}