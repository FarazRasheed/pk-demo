package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.bankslist

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingSearchRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutNoContactResultItemBinding
import com.yap.yappk.databinding.PkLayoutBanksListItemBinding
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_no_contact_result
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.di.ResourcesProviders
import java.util.*

class BanksListAdapter(
    private var banksList: MutableList<BankData>,
    val resourcesProviders: ResourcesProviders? = null,
) :
    BaseBindingSearchRecyclerAdapter<BankData, BanksListAdapter.ViewHolder>(banksList) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        viewType

    override fun onCreateViewHolder(binding: ViewDataBinding): ViewHolder {
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (banksList.isNullOrEmpty()) holder.onBind(
            BankData(),
            position,
            onItemClickListener
        ) else holder.onBind(banksList[position], position, onItemClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (banksList.isNullOrEmpty())
            R.layout.layout_no_contact_result_item
        else R.layout.pk_layout_banks_list_item
    }

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            bankData: BankData,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is PkLayoutBanksListItemBinding -> {
                    handleBanksListBinding(
                        pkBanksListBinding = binding,
                        bankData = bankData,
                        position = position,
                        onItemClickListener = onItemClickListener
                    )
                }
                is LayoutNoContactResultItemBinding -> {
                    handleNoContactResultBinding(binding)
                }
            }

            binding.executePendingBindings()
        }
    }

    private fun handleBanksListBinding(
        pkBanksListBinding: PkLayoutBanksListItemBinding,
        bankData: BankData,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        pkBanksListBinding.tvBankName.text =
            bankData.bankName
        pkBanksListBinding.ivBankLogo.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankData.bankName ?: "")
                .setIndex(position)
                .setImageUrl(bankData.bankLogoUrl)
                .build()
        )
        pkBanksListBinding.clMain.setOnClickListener {
            onItemClickListener?.onItemClick(it, bankData, position)
        }
    }

    private fun handleNoContactResultBinding(
        binding: LayoutNoContactResultItemBinding
    ) {
        binding.tvNoResult.text =
            resourcesProviders?.getString(screen_send_yap_to_yap_display_text_no_contact_result)
    }

    override fun getItemCount(): Int = if (banksList.isNullOrEmpty()) 1 else banksList.size

    override fun filterItem(constraint: CharSequence?, item: BankData): Boolean {
        val filterString = constraint.toString().lowercase(Locale.ROOT)
        val bankName = item.bankName?.lowercase(Locale.ROOT) ?: ""
        return bankName.contains(filterString)
    }
}