package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard

import android.view.View
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.cards.responsedtos.CardDesignColor

class ColorCircleItemViewModel(
    var colorDesigns: CardDesignColor,
    var position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, colorDesigns, position)
    }
}
