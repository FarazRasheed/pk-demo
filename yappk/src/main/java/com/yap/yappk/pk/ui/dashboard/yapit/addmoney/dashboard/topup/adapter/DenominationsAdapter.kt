package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.R
import com.yap.yappk.databinding.PkItemDenominationsBinding
import com.yap.yappk.localization.common_top_up_denomination_amount
import com.yap.yappk.networking.microservices.transactions.responsedtos.FundTransferDenominations
import com.yap.yappk.pk.di.ResourcesProviders

class DenominationsAdapter(
    private var denominationList: MutableList<FundTransferDenominations>,
    private var resourcesProviders: ResourcesProviders
) :
    BaseBindingRecyclerAdapter<FundTransferDenominations, RecyclerView.ViewHolder>(denominationList) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.pk_item_denominations

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return ViewHolder(
            binding as PkItemDenominationsBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            holder.onBind(denominationList[position], position, onItemClickListener, resourcesProviders)
        }
    }

    class ViewHolder(private val pkItemDenominationsBinding: PkItemDenominationsBinding) :
        RecyclerView.ViewHolder(pkItemDenominationsBinding.root) {

        fun onBind(
            denomination: FundTransferDenominations,
            position: Int,
            onItemClickListener: OnItemClickListener?,
            resourcesProviders: ResourcesProviders
        ) {
            pkItemDenominationsBinding.tvItemDenomination.text =
                resourcesProviders.getString(common_top_up_denomination_amount, denomination.amount)
            pkItemDenominationsBinding.clItemMain.setOnClickListener {
                onItemClickListener?.onItemClick(it, denomination, position)
            }
            pkItemDenominationsBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = denominationList.size
}