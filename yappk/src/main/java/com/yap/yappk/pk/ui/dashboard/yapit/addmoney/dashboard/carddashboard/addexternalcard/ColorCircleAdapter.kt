package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.uikit.utils.extensions.getColors
import com.yap.yappk.R
import com.yap.yappk.databinding.PkItemCircleViewBinding
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDesignColor
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDesigns
import com.yap.yappk.pk.utils.CircleView

class ColorCircleAdapter(
    private val list: MutableList<CardDesigns>
) :
    BaseBindingRecyclerAdapter<CardDesigns, RecyclerView.ViewHolder>(list) {
    override fun getLayoutIdForViewType(viewType: Int): Int =
        R.layout.pk_item_circle_view

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return ViewHolder(
            binding as PkItemCircleViewBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            holder.onBind(list[position], position, onItemClickListener)
        }
    }

    class ViewHolder(private val itemCircleViewBinding: PkItemCircleViewBinding) :
        RecyclerView.ViewHolder(itemCircleViewBinding.root) {
        fun onBind(
            colorDesigns: CardDesigns,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            try {
                itemCircleViewBinding.circleView.circleColorStart =
                    Color.parseColor(colorDesigns.designCodeColors?.firstOrNull()?.colorCode)
                itemCircleViewBinding.circleView.circleColorEnd =
                    Color.parseColor(colorDesigns.designCodeColors?.get(1)?.colorCode)
                itemCircleViewBinding.circleView.circleColorDirection = CircleView.GradientDirection.TOP_TO_BOTTOM
                if (colorDesigns.isSelected == true) {
                    itemCircleViewBinding.circleView.borderWidth = 8f
                    itemCircleViewBinding.circleView.borderColorDirection =
                        CircleView.GradientDirection.TOP_TO_BOTTOM
                    try {
                        itemCircleViewBinding.circleView.borderColorStart =
                            Color.parseColor(
                                colorDesigns.designCodeColors?.firstOrNull()?.colorCode
                            )
                        itemCircleViewBinding.circleView.borderColorEnd =
                            Color.parseColor(
                                colorDesigns.designCodeColors?.get(1)?.colorCode
                            )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    itemCircleViewBinding.circleView.borderWidth = 0f
                    itemCircleViewBinding.circleView.borderColor =
                        itemCircleViewBinding.circleView.context.getColors(R.color.light_grey)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            itemCircleViewBinding.viewModel =
                ColorCircleItemViewModel(
                    CardDesignColor(),
                    position,
                    onItemClickListener
                )
            itemCircleViewBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = list.size
}
