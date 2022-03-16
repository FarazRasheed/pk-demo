package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails.success

import androidx.viewbinding.ViewBinding
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.databinding.PkLayoutAddBeneficiarySuccessDialogViewBinding
import com.yap.yappk.pk.utils.BaseViewHolder

class AddBeneficiarySuccessDialogVH(
    private val itemClickListener: OnItemClickListener
) : BaseViewHolder {
    private var mViewBinding: ViewBinding? = null
    override fun onBind(viewBinding: ViewBinding?) {
        mViewBinding = viewBinding
        if (viewBinding is PkLayoutAddBeneficiarySuccessDialogViewBinding) {
            viewBinding.btnTransfer.setOnClickListener {
                itemClickListener.onItemClick(view = it, "", 0)
            }
            viewBinding.btnSendLater.setOnClickListener {
                itemClickListener.onItemClick(view = it, "", 1)
            }
        }
    }

    override fun notifyDatasetRefresh(data: Any) = Unit
}