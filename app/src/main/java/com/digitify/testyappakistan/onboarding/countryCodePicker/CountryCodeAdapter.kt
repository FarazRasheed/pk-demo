package com.digitify.testyappakistan.onboarding.countryCodePicker

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.LayoutBottomBinding
import com.digitify.testyappakistan.databinding.LayoutBottomSheetItemBinding

class CountryCodeAdapter(
    private val list: MutableList<CountryItem>
) : BaseBindingRecyclerAdapter<CountryItem, CountryCodeAdapter.WaitingListItemViewHolder>(
    list
) {
    override fun onCreateViewHolder(binding: ViewDataBinding): WaitingListItemViewHolder {
        return WaitingListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaitingListItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }

    override fun getLayoutIdForViewType(viewType: Int): Int {
        return when (viewType) {
            0 -> {
                R.layout.layout_bottom
            }
            else -> R.layout.layout_bottom_sheet_item
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class WaitingListItemViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: CountryItem,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is LayoutBottomBinding -> {
                    binding.viewModel = data
                }

                is LayoutBottomSheetItemBinding -> {
                    binding.viewModel = data
                    binding.llMain?.setOnClickListener {
                        onItemClickListener?.onItemClick(it, data, position)
                    }
                }

            }
            binding.executePendingBindings()
        }
    }
}