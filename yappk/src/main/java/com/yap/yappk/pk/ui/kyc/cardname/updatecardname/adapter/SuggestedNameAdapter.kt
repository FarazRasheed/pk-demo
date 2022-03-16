package com.yap.yappk.pk.ui.kyc.cardname.updatecardname.adapter

import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutNameItemBinding

class SuggestedNameAdapter(
    private val list: MutableList<String>
) : BaseBindingRecyclerAdapter<String, SuggestedNameListViewHolder>(
    list
) {

    var selectedPosition: ObservableInt = ObservableInt(0)

    override fun onCreateViewHolder(binding: ViewDataBinding): SuggestedNameListViewHolder {
        return SuggestedNameListViewHolder(binding as LayoutNameItemBinding)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.layout_name_item

    override fun onBindViewHolder(holder: SuggestedNameListViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], selectedPosition.get(), position)
    }

}