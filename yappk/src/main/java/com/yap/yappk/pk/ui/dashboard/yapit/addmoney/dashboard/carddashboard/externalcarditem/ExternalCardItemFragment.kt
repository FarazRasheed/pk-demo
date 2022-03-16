package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarditem

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseFragment
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.visible
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentExternalCardItemBinding
import com.yap.yappk.localization.common_masked_card_number
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerListener
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardColor
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardExpiry
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.getCardLogoByType
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension.isExpired
import com.yap.yappk.pk.utils.widgets.PaymentCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExternalCardItemFragment(
    val position: Int? = null,
    val externalCard: ExternalCard,
    val isShowJustCard: Boolean,
    val callback: ExternalCardPagerListener? = null,
) : BaseFragment<FragmentExternalCardItemBinding, IExternalCardItem.State, IExternalCardItem.ViewModel>(
    R.layout.fragment_external_card_item
),
    IExternalCardItem.View,
    PaymentCard.OnPaymentCardClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IExternalCardItem.ViewModel by viewModels<ExternalCardItemVM>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues(externalCard)
    }

    private fun setValues(externalCard: ExternalCard) {
        mViewBinding.pcCard.setOnPaymentCardClickListener(this)
        mViewBinding.pcCard.paymentCardBackgroundColor = externalCard.getCardColor(requireContext())
        mViewBinding.pcCard.paymentCardNumber =
            requireContext().getString(common_masked_card_number, externalCard.number ?: "")
        mViewBinding.pcCard.paymentCardDate = externalCard.getCardExpiry(requireContext())
        mViewBinding.pcCard.paymentCardLogoIcon = externalCard.getCardLogoByType(requireContext())
        mViewBinding.pcCard.paymentCardName = externalCard.alias
        if (externalCard.isExpired(requireContext())) {
            mViewBinding.clCardStatus.visible()
            mViewBinding.pcCard.paymentCardInfoIconVisible = false
        } else {
            mViewBinding.clCardStatus.invisible()
            mViewBinding.pcCard.paymentCardInfoIconVisible = true
        }
        if (isShowJustCard) {
            mViewBinding.clCardStatus.invisible()
            mViewBinding.pcCard.paymentCardInfoIconVisible = false
        }
    }

    override fun onInfoIconClicked(view: View) {
        callback?.openCardDetailScreen()
    }

    override fun onCardClicked(view: View) {
        callback?.openExternalCardTopUp(externalCard)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.clCardStatus -> callback?.openCardDetailScreen()
        }
    }
}