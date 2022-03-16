package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.carddetaildialog

import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.shortToast
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutCardDetailDialogViewBinding
import com.yap.yappk.localization.common_text_copied
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.utils.BaseViewHolder
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import com.yap.yappk.pk.utils.copyToClipboard

class CardDetailDialogVH(
    var data: CardDetail,
    private val card: Card?,
    private val itemClickListener: OnItemClickListener,
    private val reloadListener: MultiStateLayout.OnReloadListener
) : BaseViewHolder {
    private var mViewBinding: ViewBinding? = null
    override fun onBind(viewBinding: ViewBinding?) {
        mViewBinding = viewBinding
        if (viewBinding is LayoutCardDetailDialogViewBinding) {
            viewBinding.msvStateView.viewState = data.state
            viewBinding.ivClose.setOnClickListener {
                itemClickListener.onItemClick(view = it, data, 0)
            }
            viewBinding.ivCard.loadImage(
                card?.frontImage,
                ContextCompat.getDrawable(
                    viewBinding.ivCard.context,
                    card?.getCardImageResource() ?: R.drawable.pk_card_spare
                ),
                ContextCompat.getDrawable(
                    viewBinding.ivCard.context,
                    R.drawable.pk_bg_card_place_holder
                )
            )
            viewBinding.tvCardNo.text = data.cardToken
            viewBinding.tvCardName.text = data.title
            viewBinding.tvCardType.text = data.cardType
            viewBinding.tvExpire.text = data.expiry
            viewBinding.tvCvv.text = data.cvv2
            viewBinding.msvStateView.setOnReloadListener(reloadListener)
            viewBinding.tvCardNoCopy.setOnClickListener { view ->
                viewBinding.root.context.copyToClipboard(data.cardToken ?: "")
                viewBinding.root.context.shortToast(
                    viewBinding.root.context.getString(
                        common_text_copied
                    )
                )
            }
            viewBinding.executePendingBindings()
        }

    }

    override fun notifyDatasetRefresh(data: Any) {
        if (data is CardDetail) {
            this.data = data
            onBind(mViewBinding)
        }
    }

}