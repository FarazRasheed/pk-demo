package com.yap.yappk.pk.ui.dashboard.cards.cardstatus.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.yap.uikit.widget.TimelineView
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemCardDeliveryStatusBinding

class CardDeliveryStatusAdapter(
    private val list: MutableList<StatusDataModel>
) : BaseBindingRecyclerAdapter<StatusDataModel, CardDeliveryStatusAdapter.CardDeliveryStatusViewHolder>(
    list
) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.layout_item_card_delivery_status

    override fun onCreateViewHolder(binding: ViewDataBinding): CardDeliveryStatusViewHolder {
        return CardDeliveryStatusViewHolder(
            binding as LayoutItemCardDeliveryStatusBinding,
            0
        )
    }

    override fun onCreateViewHolder(
        binding: ViewDataBinding,
        viewType: Int
    ): CardDeliveryStatusViewHolder {
        return CardDeliveryStatusViewHolder(
            binding as LayoutItemCardDeliveryStatusBinding,
            viewType
        )
    }

    override fun onBindViewHolder(holder: CardDeliveryStatusViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(position, list[position])

    }

    class CardDeliveryStatusViewHolder(
        private val binding: LayoutItemCardDeliveryStatusBinding,
        private val viewType: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timeline.initLine(viewType)
        }

        @SuppressLint("ResourceAsColor")
        fun onBind(
            position: Int,
            statusDataModel: StatusDataModel
        ) {
            val marker = if (statusDataModel.markerInProgress) ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.pk_ic_tick_disabled
            ) else ContextCompat.getDrawable(binding.root.context, R.drawable.pk_ic_filled_tick)
            binding.timeline.setEndLineColor(
                if (statusDataModel.lineInProgress) ContextCompat.getColor(
                    binding.root.context,
                    R.color.pkPaleLilac
                ) else ContextCompat.getColor(binding.root.context, R.color.pkBlueWithAHintOfPurple),
                viewType
            )
            binding.viewModel = statusDataModel
            binding.timeline.marker = marker
            binding.tvStatus.text = statusDataModel.statusTitle
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

}
