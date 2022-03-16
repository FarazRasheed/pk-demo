package com.yap.yappk.pk.ui.generic.recents

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.R
import com.yap.yappk.databinding.PkItemCoreRecentBeneficiaryBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary

class CoreRecentTransferAdapter(
    private val list: MutableList<IBeneficiary>
) :
    BaseBindingRecyclerAdapter<IBeneficiary, RecyclerView.ViewHolder>(list) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.pk_item_core_recent_beneficiary

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return ViewHolder(
            binding as PkItemCoreRecentBeneficiaryBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            holder.onBind(list[position], position, onItemClickListener)
        }
    }

    class ViewHolder(private val itemCoreRecentBeneficiaryBinding: PkItemCoreRecentBeneficiaryBinding) :
        RecyclerView.ViewHolder(itemCoreRecentBeneficiaryBinding.root) {
        fun onBind(
            coreRecentBeneficiary: IBeneficiary,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (coreRecentBeneficiary.userType) {
                "Y2Y" -> {
                    itemCoreRecentBeneficiaryBinding.ivPackage.visibility = View.VISIBLE
                    itemCoreRecentBeneficiaryBinding.ivPackage.setImageResource(R.drawable.pk_ic_standard_package)
                }
                else -> {
                    itemCoreRecentBeneficiaryBinding.ivPackage.visibility = View.GONE
                }
            }

            itemCoreRecentBeneficiaryBinding.coreView.niIndex = position
            itemCoreRecentBeneficiaryBinding.viewModel =
                CoreRecentBeneficiaryItemViewModel(
                    coreRecentBeneficiary,
                    position,
                    onItemClickListener
                )
            itemCoreRecentBeneficiaryBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = if (list.size > 15) {
        15
    } else list.size

}