package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.yapcontacts.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingSearchRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.clickableSpan
import com.digitify.core.extensions.getColors
import com.digitify.core.extensions.gone
import com.digitify.core.extensions.visible
import com.yap.uikit.utils.helpers.getFormattedPhoneNumber
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutNoContactItemBinding
import com.yap.yappk.databinding.LayoutNoContactResultItemBinding
import com.yap.yappk.databinding.LayoutYapContactItemBinding
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_no_contact
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_no_contact_result
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_yap_contacts_count
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.contacts.ContactDiffCallback
import java.util.*

class YapContactListAdapter(
    private val list: MutableList<Contact>,
    val resourcesProviders: ResourcesProviders,
    val referralAmount: ReferralAmount?,
    val isSearchView: Boolean
) :
    BaseBindingSearchRecyclerAdapter<Contact, YapContactListAdapter.YapContactItemViewHolder>(
        list
    ) {
    override fun onCreateViewHolder(binding: ViewDataBinding): YapContactItemViewHolder {
        return YapContactItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YapContactItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (list.isNullOrEmpty()) holder.onBind(
            Contact(),
            position,
            onItemClickListener
        ) else holder.onBind(list[position], position, onItemClickListener)
    }


    override fun getItemViewType(position: Int): Int {
        return if (list.isNullOrEmpty()) {
            if (isSearchView)
                R.layout.layout_no_contact_result_item
            else R.layout.layout_no_contact_item
        } else R.layout.layout_yap_contact_item
    }

    override fun getItemCount(): Int = if (list.isNullOrEmpty()) 1 else list.size
    inner class YapContactItemViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: Contact,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is LayoutNoContactItemBinding -> {
                    handleNoContactBinding(binding, position, onItemClickListener)
                }
                is LayoutNoContactResultItemBinding -> {
                    handleNoContactResultBinding(binding)
                }
                is LayoutYapContactItemBinding -> {
                    handleYapContactBinding(binding, data, position, onItemClickListener)
                }
            }
            binding.executePendingBindings()
        }
    }

    private fun handleNoContactResultBinding(
        binding: LayoutNoContactResultItemBinding

    ) {
        binding.tvNoResult.text =
            resourcesProviders.getString(screen_send_yap_to_yap_display_text_no_contact_result)
    }

    private fun handleYapContactBinding(
        binding: LayoutYapContactItemBinding,
        data: Contact,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        if (position == 0) {
            binding.tvTitle.text = String.format(
                resourcesProviders.getString(
                    screen_send_yap_to_yap_display_text_yap_contacts_count,
                    "${list.size}"
                )
            )
            binding.tvTitle.visible()
        } else {
            binding.tvTitle.gone()
        }
        binding.ivImage.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(data.title ?: "")
                .setIndex(position)
                .setImageUrl(data.beneficiaryPictureUrl)
                .build()
        )
        binding.tvName.text = data.title
        binding.tvPhoneNumber.text = getFormattedPhoneNumber(
            binding.root.context,
            data.countryCode?.replace("+", "00") + data.mobileNo
        )
        binding.clMain.setOnClickListener {
            onItemClickListener?.onItemClick(it, data, position)
        }
    }

    private fun handleNoContactBinding(
        binding: LayoutNoContactItemBinding,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        binding.tvDescription.text = String.format(
            resourcesProviders.getString(
                screen_send_yap_to_yap_display_text_no_contact,
                "${referralAmount?.currencyCode ?: "PKR"} ${referralAmount?.referralAmount ?: "00"}"
            )
        )
        binding.tvDescription.clickableSpan(
            Pair(
                "${referralAmount?.currencyCode ?: "PKR"} ${referralAmount?.referralAmount ?: "00"}",
                View.OnClickListener {

                }),
            color = binding.root.context.getColors(R.color.pkBlueWithAHintOfPurple),
            underline = false
        )
        binding.btnInviteNow.setOnClickListener {
            onItemClickListener?.onItemClick(it, "", position)
        }
    }


    override fun getLayoutIdForViewType(viewType: Int): Int {
        return viewType
    }

    override fun filterItem(constraint: CharSequence?, item: Contact): Boolean {
        val filterString = constraint.toString().lowercase(Locale.ROOT)
        val contactName = item.title?.lowercase(Locale.ROOT) ?: ""
        return contactName.startsWith(filterString)
    }

    fun updateContactListItems(contactList: List<Contact>) {
        val diffCallback = ContactDiffCallback(list, contactList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.setList(contactList)
        diffResult.dispatchUpdatesTo(this)
    }

}