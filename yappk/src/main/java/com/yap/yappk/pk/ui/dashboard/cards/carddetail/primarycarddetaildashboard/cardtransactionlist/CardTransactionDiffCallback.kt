package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionlist

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction

class CardTransactionDiffCallback(
    val oldList: List<Transaction>,
    val newList: List<Transaction>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old == new
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}