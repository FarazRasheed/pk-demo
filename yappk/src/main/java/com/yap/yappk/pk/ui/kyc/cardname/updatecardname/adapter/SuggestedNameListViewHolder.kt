package com.yap.yappk.pk.ui.kyc.cardname.updatecardname.adapter

import androidx.recyclerview.widget.RecyclerView
import com.yap.yappk.databinding.LayoutNameItemBinding

class SuggestedNameListViewHolder(private val itemBinding: LayoutNameItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun onBind(
        data: String,
        selectedPosition: Int,
        position: Int
    ) {
        itemBinding.cbName.text = data
        itemBinding.cbName.isChecked = selectedPosition == position
    }
}