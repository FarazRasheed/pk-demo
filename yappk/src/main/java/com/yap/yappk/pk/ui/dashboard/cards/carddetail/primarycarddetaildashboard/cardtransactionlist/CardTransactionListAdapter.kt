package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionlist

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.visible
import com.yap.uikit.utils.extensions.gone
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutCardTransactionItemBinding
import com.yap.yappk.localization.screen_card_detail_display_text_list_time
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.TransactionType
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getFormattedDate
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getFormattedTime
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getTransactionIcon

class CardTransactionListAdapter(
    private val list: MutableList<Transaction>,
    val resourcesProviders: ResourcesProviders
) :
    BaseBindingRecyclerAdapter<Transaction, CardTransactionListAdapter.CardTransactionItemViewHolder>(
        list
    ) {
    override fun onCreateViewHolder(binding: ViewDataBinding): CardTransactionItemViewHolder {
        return CardTransactionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardTransactionItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int {
        return R.layout.layout_card_transaction_item
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CardTransactionItemViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: Transaction,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is LayoutCardTransactionItemBinding -> {
                    binding.viewModel =
                        CardTransactionItemViewModel(
                            data,
                            position,
                            onItemClickListener
                        )
                    if (data.type == 1) {
                        binding.tvDate.text = data.getFormattedDate()
                        binding.tvTotal.text = data.totalAmount
                        binding.tvTotal.visible()
                        binding.tvDate.visible()

                    }else{
                        binding.tvTotal.gone()
                        binding.tvDate.gone()
                    }

                    binding.ivIcon.setImageInfo(
                        NameInitialsCircleImageView.ImageInfo
                            .Builder(data.title ?: "")
                            .setIndex(position)
                            .build()
                    )
                    binding.ivIcon.setImageResource(data.getTransactionIcon())
                    binding.tvTitle.text = data.title
                    binding.tvAmount.text =
                        if (data.transactionType == TransactionType.DEBIT.name) "-" + data.amount else "+" + data.amount
                    binding.tvCurrency.text = data.currency
                    binding.tvTime.text = resourcesProviders.getString(
                        screen_card_detail_display_text_list_time,
                        data.getFormattedTime() ?: "",
                        data.transactionCategory ?: ""
                    )
                }
            }
            binding.executePendingBindings()
        }
    }

    fun updateCardListItems(transactionList: List<Transaction>) {
        val diffCallback = CardTransactionDiffCallback(list, transactionList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.setList(transactionList)
        diffResult.dispatchUpdatesTo(this)
    }
}