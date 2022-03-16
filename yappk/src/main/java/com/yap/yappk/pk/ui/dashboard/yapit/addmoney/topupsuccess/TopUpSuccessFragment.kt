package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.topupsuccess

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentTopUpSuccessBinding
import com.yap.yappk.localization.common_masked_card_number
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.utils.KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopUpSuccessFragment :
    BaseNavFragment<FragmentTopUpSuccessBinding, ITopUpSuccess.State, ITopUpSuccess.ViewModel>(
        R.layout.fragment_top_up_success
    ),
    ITopUpSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: ITopUpSuccess.ViewModel by viewModels<TopUpSuccessVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.getAccountBalance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        setBackButtonDispatcher()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnGoToDashboard ->
                navigateWithPopup(R.id.cardDashboardFragment, R.id.cardDashboardFragment)
        }
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setCard(it.getParcelable(TopUpSuccessFragment::class.java.name))
            viewModel.setTopUpAmount(it.getString(KEY))
        }
    }

    private fun setCardValues(externalCard: ExternalCard) {
        mViewBinding.pcCard.paymentCardBackgroundColor = externalCard.getCardColor(requireContext())
        mViewBinding.pcCard.paymentCardNumber =
            requireContext().getString(common_masked_card_number, externalCard.number ?: "")
        mViewBinding.pcCard.paymentCardDate = externalCard.getCardExpiry(requireContext())
        mViewBinding.pcCard.paymentCardLogoIcon = externalCard.getCardLogoByType(requireContext())
        mViewBinding.pcCard.paymentCardName = externalCard.alias
        mViewBinding.tvCardNumber.text =
            requireContext().getString(common_masked_card_number, externalCard.number ?: "")
        mViewBinding.tvCardName.text = externalCard.alias
    }

    private fun handleCard(card: ExternalCard?) {
        card?.let {
            setCardValues(it)
        }
    }

    private fun handleAvailableBalance(balance: String?) {
        balance?.let { availableBalance ->
            mViewBinding.tvBalance.text = availableBalance
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.card, ::handleCard)
        observe(viewModel.availableBalance, ::handleAvailableBalance)
    }
}