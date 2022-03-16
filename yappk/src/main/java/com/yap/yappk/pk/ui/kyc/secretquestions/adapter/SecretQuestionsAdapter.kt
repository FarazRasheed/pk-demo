package com.yap.yappk.pk.ui.kyc.secretquestions.adapter

import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.yap.yappk.R
import com.yap.yappk.databinding.ItemMotherMaidenNameBinding

class SecretQuestionsAdapter(
    private val list: MutableList<String>
) : BaseBindingRecyclerAdapter<String, SecretQuestionItemViewHolder>(
    list
) {

    var selectedPosition: ObservableInt = ObservableInt(RecyclerView.NO_POSITION)

    override fun onCreateViewHolder(binding: ViewDataBinding): SecretQuestionItemViewHolder {
        return SecretQuestionItemViewHolder(binding as ItemMotherMaidenNameBinding)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_mother_maiden_name

    override fun onBindViewHolder(holder: SecretQuestionItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], selectedPosition.get(), position)
    }

}