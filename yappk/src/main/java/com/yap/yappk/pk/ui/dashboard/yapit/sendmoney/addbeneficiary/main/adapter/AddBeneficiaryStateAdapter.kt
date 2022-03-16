package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.yap.uikit.widget.TimelineView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemAddBeneficiaryStatusBinding

class AddBeneficiaryStateAdapter(
    private val list: MutableList<AddBeneficiaryStateModel>
) : BaseBindingRecyclerAdapter<AddBeneficiaryStateModel, AddBeneficiaryStateAdapter.AddBeneficiaryStateViewHolder>(
    list
) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.layout_item_add_beneficiary_status

    override fun onCreateViewHolder(binding: ViewDataBinding): AddBeneficiaryStateViewHolder {
        return AddBeneficiaryStateViewHolder(
            binding as LayoutItemAddBeneficiaryStatusBinding,
            0
        )
    }

    override fun onCreateViewHolder(
        binding: ViewDataBinding,
        viewType: Int
    ): AddBeneficiaryStateViewHolder {
        return AddBeneficiaryStateViewHolder(
            binding as LayoutItemAddBeneficiaryStatusBinding,
            viewType
        )
    }

    override fun onBindViewHolder(holder: AddBeneficiaryStateViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(position, list[position])

    }

    class AddBeneficiaryStateViewHolder(
        private val binding: LayoutItemAddBeneficiaryStatusBinding,
        private val viewType: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timeline.initLine(viewType)
        }

        @SuppressLint("ResourceAsColor")
        fun onBind(
            position: Int,
            statusDataModel: AddBeneficiaryStateModel
        ) {
            val marker =
                when (statusDataModel.markerState) {
                    AddBeneficiaryStates.IN_PROGRESS.name -> if (position == 0) ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.pk_ic_one_filled_circle
                    ) else if (position == 1) ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.pk_ic_two_filled_circle
                    ) else ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.pk_ic_three_filled_circle
                    )
                    AddBeneficiaryStates.PENDING.name -> ContextCompat.getDrawable(
                        binding.root.context,
                        if (position == 1) R.drawable.pk_ic_two_un_filled_circle else R.drawable.pk_ic_three_un_filled_circle
                    )
                    else -> ContextCompat.getDrawable(binding.root.context, R.drawable.pk_ic_filled_tick)
                }
            binding.timeline.setEndLineColor(
                ContextCompat.getColor(binding.root.context, R.color.pkLightBlueGrey),
                viewType
            )
            binding.viewModel = statusDataModel
            binding.timeline.marker = marker
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

}
