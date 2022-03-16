package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarddetail

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentExternalCardDetailBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.utils.widgets.PaymentCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExternalCardDetailFragment :
    BaseNavFragment<FragmentExternalCardDetailBinding, IExternalCardDetail.State, IExternalCardDetail.ViewModel>(
        R.layout.fragment_external_card_detail
    ), IExternalCardDetail.View, ToolBarView.OnToolBarViewClickListener,
    PaymentCard.OnPaymentCardClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IExternalCardDetail.ViewModel by viewModels<ExternalCardDetailVM>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        getFragmentArguments()
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setCard(it.getParcelable<ExternalCard>(ExternalCardDetailFragment::class.java.name))
        }
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnRemove -> showAlertRemoveCard()
        }
    }

    private fun showAlertRemoveCard() {
        confirm(
            message = requireContext().getString(
                screen_external_card_detail_display_text_alert_msg
            ),
            positiveButton = requireContext().getString(
                screen_external_card_detail_display_text_remove
            ),
            negativeButton = requireContext().getString(common_button_cancel),
            cancelable = false
        ) {
            viewModel.deleteExternalCard(viewModel.card.value?.id ?: "")
        }
    }

    override fun onCardClicked(view: View) {
        // Later
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    private fun handleCard(card: ExternalCard?) {
        card?.let {
            setCardValues(it)
        }
    }

    private fun handleCardDeletion(isCardDeleted: Boolean) {
        if (isCardDeleted)
            navigateBackWithResult(resultCode = Activity.RESULT_OK)
    }

    private fun setCardValues(externalCard: ExternalCard) {
        mViewBinding.pcCard.paymentCardBackgroundColor = externalCard.getCardColor(requireContext())
        mViewBinding.pcCard.paymentCardNumber =
            requireContext().getString(common_masked_card_number, externalCard.number ?: "")
        mViewBinding.pcCard.paymentCardDate = externalCard.getCardExpiry(requireContext())
        mViewBinding.pcCard.paymentCardLogoIcon = externalCard.getCardLogoByType(requireContext())
        mViewBinding.pcCard.paymentCardName = externalCard.alias
        mViewBinding.tvCardExpiry.text = externalCard.getCardExpiry(requireContext())
        mViewBinding.tvCardType.text = externalCard.logo
        mViewBinding.tvCardNumber.text =
            requireContext().getString(common_masked_card_number, externalCard.number ?: "")
        mViewBinding.tvCardName.text = externalCard.alias
    }

    override fun viewModelObservers() {
        observe(viewModel.card, ::handleCard)
        observe(viewModel.isCardDeleted, ::handleCardDeletion)
    }

}