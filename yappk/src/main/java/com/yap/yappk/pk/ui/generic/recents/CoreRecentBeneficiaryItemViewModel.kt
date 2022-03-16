package com.yap.yappk.pk.ui.generic.recents

import android.view.View
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary

class CoreRecentBeneficiaryItemViewModel(
    var coreRecentBeneficiary: IBeneficiary,
    var position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, coreRecentBeneficiary, position)
    }
}
