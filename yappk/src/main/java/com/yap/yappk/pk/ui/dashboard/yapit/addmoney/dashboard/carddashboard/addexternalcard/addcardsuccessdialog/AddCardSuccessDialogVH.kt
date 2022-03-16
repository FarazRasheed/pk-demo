package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard.addcardsuccessdialog

import androidx.viewbinding.ViewBinding
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.databinding.LayoutCardAddedSuccessDialogViewBinding
import com.yap.yappk.pk.utils.BaseViewHolder

class AddCardSuccessDialogVH(
    private val itemClickListener: OnItemClickListener
) : BaseViewHolder {
    private var mViewBinding: ViewBinding? = null
    override fun onBind(viewBinding: ViewBinding?) {
        mViewBinding = viewBinding
        if (viewBinding is LayoutCardAddedSuccessDialogViewBinding) {
            viewBinding.btnTopUp.setOnClickListener {
                itemClickListener.onItemClick(view = it, "", 0)
            }
            viewBinding.btnDoItLater.setOnClickListener {
                itemClickListener.onItemClick(view = it, "", 1)
            }
        }
    }

    override fun notifyDatasetRefresh(data: Any) = Unit
}