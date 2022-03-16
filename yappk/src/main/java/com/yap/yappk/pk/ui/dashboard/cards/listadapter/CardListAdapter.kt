package com.yap.yappk.pk.ui.dashboard.cards.listadapter

import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.visible
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutDebitCardItemBinding
import com.yap.yappk.databinding.LayoutVirtualCardItemBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.enums.CardType
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.utils.ImageBinder.loadImage

class CardListAdapter(private val list: MutableList<Card>) :
    BaseBindingRecyclerAdapter<Card, CardListAdapter.CardItemViewHolder>(
        list
    ) {
    override fun onCreateViewHolder(binding: ViewDataBinding): CardItemViewHolder {
        return CardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int {
        return when {
            list[viewType].cardType == CardType.DEBIT.name -> {
                R.layout.layout_debit_card_item;
            }
            else -> R.layout.layout_virtual_card_item;
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CardItemViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: Card,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is LayoutDebitCardItemBinding -> {
                    binding.viewModel =
                        CardItemViewModel(
                            data,
                            position,
                            onItemClickListener
                        )
                    binding.ivCard.loadImage(
                        data.frontImage,
                        ContextCompat.getDrawable(binding.root.context, data.getCardImageResource()),
                        ContextCompat.getDrawable(binding.root.context, R.drawable.pk_bg_card_place_holder)
                    )
                }

                is LayoutVirtualCardItemBinding -> {
                    binding.viewModel =
                        CardItemViewModel(
                            data,
                            position,
                            onItemClickListener
                        )
                    if (position + 1 == list.size)
                        binding.vLine.gone()
                    else
                        binding.vLine.visible()
                }

            }
            binding.executePendingBindings()
        }
    }

    fun updateCardListItems(cardsList: List<Card>) {
        val diffCallback = CardDiffCallback(list, cardsList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.setList(cardsList)
        diffResult.dispatchUpdatesTo(this)
    }
}