package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.globalsearch.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingSearchRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.invisible
import com.digitify.core.extensions.visible
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutNoContactResultItemBinding
import com.yap.yappk.databinding.LayoutSearchContactItemBinding
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_no_contact_result
import com.yap.yappk.networking.microservices.customers.responsedtos.Beneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import java.util.*

class BeneficiaryListAdapter(
    private val list: MutableList<IBeneficiary>,
    val resourcesProviders: ResourcesProviders,
) :
    BaseBindingSearchRecyclerAdapter<IBeneficiary, BeneficiaryListAdapter.ViewHolder>(list) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        viewType

    override fun onCreateViewHolder(binding: ViewDataBinding): ViewHolder {
        return ViewHolder(
            binding
        )
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
            R.layout.layout_no_contact_result_item
        else R.layout.layout_search_contact_item
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
                is LayoutSearchContactItemBinding -> {
                    handleSearchBinding(binding, data, position, onItemClickListener)
                }
            }
            binding.executePendingBindings()
        }
    }

    private fun handleSearchBinding(
        binding: LayoutSearchContactItemBinding,
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
        when (data.userType) {
            "Y2Y" -> {
                binding.ivYapIcon.visible()
                binding.ivBankIcon.invisible()
                binding.tvPhoneNumber.text = getFormattedPhoneNumber(
                    binding.root.context,
                    data.countryCde?.replace("+", "00") + data.mobileNumber
                )
            }
            else -> {
                binding.ivYapIcon.invisible()
                binding.ivBankIcon.visible()
                binding.tvPhoneNumber.text = data.subtitle
            }
        }
        binding.tvName.text = data.fullName
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