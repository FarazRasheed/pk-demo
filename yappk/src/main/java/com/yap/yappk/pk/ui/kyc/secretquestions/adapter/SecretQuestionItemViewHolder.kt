package com.yap.yappk.pk.ui.kyc.secretquestions.adapter

import androidx.recyclerview.widget.RecyclerView
import com.yap.yappk.databinding.ItemMotherMaidenNameBinding

class SecretQuestionItemViewHolder(private val itemBinding: ItemMotherMaidenNameBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun onBind(
        data: String,
        selectedPosition: Int,
        position: Int
    ) {
        itemBinding.tvName.text = data
        itemBinding.tvName.isChecked = selectedPosition == position
    }
}