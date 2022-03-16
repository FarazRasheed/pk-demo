package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingSearchRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.visible
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutNoContactResultItemBinding
import com.yap.yappk.databinding.PkLayoutBankBeneficiaryItemBinding
import com.yap.yappk.databinding.PkLayoutNoBankBeneficiaryBinding
import com.yap.yappk.localization.screen_main_bank_transfer_display_text_description_add
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_no_contact_result
import com.yap.yappk.networking.microservices.customers.responsedtos.Beneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import java.util.*

class BankBeneficiaryListAdapter(
    private val list: MutableList<IBeneficiary>,
    val resourcesProviders: ResourcesProviders,
    val isSearchView: Boolean = false
) :
    BaseBindingSearchRecyclerAdapter<IBeneficiary, BankBeneficiaryListAdapter.ViewHolder>(list) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        viewType

    override fun onCreateViewHolder(binding: ViewDataBinding): ViewHolder {
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (list.isNullOrEmpty()) holder.onBind(
            Beneficiary(),
            position,
            onItemClickListener
        ) else holder.onBind(list[position], position, onItemClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isNullOrEmpty())
            if (isSearchView) R.layout.layout_no_contact_result_item
            else R.layout.pk_layout_no_bank_beneficiary
        else R.layout.pk_layout_bank_beneficiary_item
    }


    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: IBeneficiary,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is LayoutNoContactResultItemBinding -> {
                    handleNoContactResultBinding(binding)
                }
                is PkLayoutBankBeneficiaryItemBinding -> {
                    handleSearchBinding(binding, data, position, onItemClickListener)
                }
                is PkLayoutNoBankBeneficiaryBinding -> {
                    handleNoBankBeneficiaryBinding(binding, onItemClickListener)
                }
            }
            binding.executePendingBindings()
        }
    }

    private fun handleNoBankBeneficiaryBinding(
        binding: PkLayoutNoBankBeneficiaryBinding,
        onItemClickListener: OnItemClickListener?
    ) {
        binding.tvNoResult.text =
            resourcesProviders.getString(screen_main_bank_transfer_display_text_description_add)
        binding.btnAdd.setOnClickListener {
            onItemClickListener?.onItemClick(it, "add", 0)
        }
    }

    private fun handleSearchBinding(
        binding: PkLayoutBankBeneficiaryItemBinding,
        data: IBeneficiary,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {

        binding.ivImage.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(data.fullName ?: "")
                .setIndex(position)
                .setImageUrl(data.imgUrl)
                .build()
        )
        binding.ivBankImage.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(data.bankTitle ?: "")
                .setImageUrl(data.bankImgUrl)
                .build()
        )

        if (position == 0 && !isSearchView) binding.tvAll.visible()
        else binding.tvAll.gone()

        binding.tvName.text = data.subtitle
        binding.tvAccountTitle.text = data.fullName
        binding.clMain.setOnClickListener {
            onItemClickListener?.onItemClick(it, data, position)
        }
    }

    private fun handleNoContactResultBinding(
        binding: LayoutNoContactResultItemBinding
    ) {
        binding.tvNoResult.text =
            resourcesProviders.getString(screen_send_yap_to_yap_display_text_no_contact_result)
    }

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 1 else list.size

    override fun filterItem(constraint: CharSequence?, item: IBeneficiary): Boolean {
        val filterString = constraint.toString().lowercase(Locale.ROOT)
        val contactName = item.fullName?.lowercase(Locale.ROOT) ?: ""
        return contactName.startsWith(filterString)
    }
}