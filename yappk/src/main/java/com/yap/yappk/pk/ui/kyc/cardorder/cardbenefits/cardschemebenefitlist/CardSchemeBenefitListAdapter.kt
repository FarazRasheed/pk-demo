package com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits.cardschemebenefitlist

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.R
import com.yap.yappk.databinding.PkLayoutSchemeBenefitItemBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.CardSchemeBenefit

class CardSchemeBenefitListAdapter(
    private val list: MutableList<CardSchemeBenefit>
) : BaseBindingRecyclerAdapter<CardSchemeBenefit, CardSchemeBenefitListAdapter.CardSchemeBenefitViewHolder>(
    list
) {
    override fun onCreateViewHolder(binding: ViewDataBinding): CardSchemeBenefitViewHolder {
        return CardSchemeBenefitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardSchemeBenefitViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.pk_layout_scheme_benefit_item

    override fun getItemCount(): Int = list.size

    inner class CardSchemeBenefitViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: CardSchemeBenefit,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is PkLayoutSchemeBenefitItemBinding -> {
                    handleCardSchemeBinding(binding, data, position, onItemClickListener)
                }
            }
            binding.executePendingBindings()
        }
    }

    private fun handleCardSchemeBinding(
        binding: PkLayoutSchemeBenefitItemBinding,
        data: CardSchemeBenefit,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        binding.tvBenefit.text = data.description
    }
}