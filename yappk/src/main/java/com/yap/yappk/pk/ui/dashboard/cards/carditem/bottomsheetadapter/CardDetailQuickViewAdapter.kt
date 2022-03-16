package com.yap.yappk.pk.ui.dashboard.cards.carditem.bottomsheetadapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.extensions.shortToast
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemCardDetailQuickViewBinding
import com.yap.yappk.localization.common_text_copied
import com.yap.yappk.localization.getString
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDetail
import com.yap.yappk.pk.utils.copyToClipboard

class CardDetailQuickViewAdapter(
    private val list: MutableList<CardDetail>,
    private val reloadListener: MultiStateLayout.OnReloadListener
) : BaseBindingRecyclerAdapter<CardDetail, CardDetailQuickViewAdapter.CardDetailViewItemViewHolder>(
    list
) {
    override fun onCreateViewHolder(binding: ViewDataBinding): CardDetailViewItemViewHolder {
        return CardDetailViewItemViewHolder(binding as LayoutItemCardDetailQuickViewBinding)
    }

    override fun onBindViewHolder(holder: CardDetailViewItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], reloadListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.layout_item_card_detail_quick_view

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CardDetailViewItemViewHolder(private val binding: LayoutItemCardDetailQuickViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CardDetail, reloadListener: MultiStateLayout.OnReloadListener) {
            binding.msvStateView.viewState = data.state
            binding.tvCardNo.text = data.cardToken
//            data.expiry?.let { expiryDate ->
//                val date = DateUtils.stringToDate(
//                    if (expiryDate.length > 6) expiryDate.removeRange(
//                        0,
//                        1
//                    ) else expiryDate, "yyMMdd"
//                )
//                binding.tvExpire.text = DateUtils.dateToString(date, "MM/yy", TimeZone.getDefault())
//            }

            binding.tvExpire.text = data.expiry
            binding.tvCvv.text = data.cvv2
            binding.tvTitle.text = data.title
            binding.msvStateView.setOnReloadListener(reloadListener)
            binding.tvCardNoCopy.setOnClickListener { view ->
                binding.root.context.copyToClipboard(data.cardToken ?: "")
                binding.root.context.shortToast(binding.root.context.getString(common_text_copied))
            }
            binding.executePendingBindings()
        }
    }

}