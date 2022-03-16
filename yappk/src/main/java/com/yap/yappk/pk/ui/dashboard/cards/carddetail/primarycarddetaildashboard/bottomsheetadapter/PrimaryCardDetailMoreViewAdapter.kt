package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.bottomsheetadapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemCardDetailMoreViewBinding

class PrimaryCardDetailMoreViewAdapter(
    private val list: MutableList<String>,
    private val rvMoreItemClickListener: OnItemClickListener
) : BaseBindingRecyclerAdapter<String, PrimaryCardDetailMoreViewAdapter.MoreViewItemViewHolder>(
    list
) {
    override fun onCreateViewHolder(binding: ViewDataBinding): MoreViewItemViewHolder {
        return MoreViewItemViewHolder(binding as LayoutItemCardDetailMoreViewBinding)
    }

    override fun onBindViewHolder(holder: MoreViewItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, rvMoreItemClickListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.layout_item_card_detail_more_view

    override fun getItemViewType(position: Int): Int = position

    inner class MoreViewItemViewHolder(private val binding: LayoutItemCardDetailMoreViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: String, position: Int, itemClickListener: OnItemClickListener) {
            binding.tvTitle.text = data
            binding.clMain.setOnClickListener { view ->
                itemClickListener.onItemClick(view, data, position)
            }
            binding.executePendingBindings()
        }
    }

}