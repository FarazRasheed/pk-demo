package com.yap.yappk.pk.ui.kyc.cardorder.cardscheme

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.StateEnum
import com.yap.uikit.widget.multiStateView.Status
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentSelectCardSchemeBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.cards.responsedtos.CardSchemeBenefit
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCardSchemeFragment :
    BaseNavFragment<FragmentSelectCardSchemeBinding, ISelectCardScheme.State, ISelectCardScheme.ViewModel>(
        R.layout.fragment_select_card_scheme
    ), ISelectCardScheme.View {

    override fun getBindingVariable(): Int = BR.viewModel
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    private val selectCardSchemeProgress = 75
    override val viewModel: ISelectCardScheme.ViewModel by viewModels<SelectCardSchemeVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.setProgressVisibility(true)
        parentViewModel.setProgress(selectCardSchemeProgress)
        setupListAdaptor()
        viewModel.getCardSchemes()
    }

    override fun onClick(id: Int) {
        when (id) {
            // Handle Later
        }
    }

    private fun handleCardSchemes(cardSchemeList: List<CardScheme>) {
        viewModel.cardSchemeAdapter.setList(cardSchemeList)
    }

    private fun setupListAdaptor() {
        mViewBinding.multiStateView.setOnReloadListener(viewModel.getReloadListener())
        viewModel.cardSchemeAdapter.onItemClickListener = rvItemClickListener
        mViewBinding.rvCardsSchemes.apply {
            adapter = viewModel.cardSchemeAdapter
        }
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is CardScheme) {
                openCardBenefitScreen(data)
            }
        }
    }

    private fun openCardBenefitScreen(data: CardScheme) {
        navigate(
            R.id.action_selectCardSchemeFragment_to_cardSchemeBenefitsFragment,
            bundleOf(CardSchemeBenefit::class.java.name to data)
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

    override fun viewModelObservers() {
        observe(viewModel.cardSchemes, ::handleCardSchemes)
        observe(viewModel.multiStateView, ::handleViewState)
    }

}