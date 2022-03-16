package com.yap.yappk.pk.ui.dashboard.cards.extensions

import android.text.format.DateFormat
import com.digitify.core.utils.DateUtils
import com.yap.yappk.R
import com.yap.yappk.networking.microservices.transactions.responsedtos.Transaction
import com.yap.yappk.pk.utils.enums.TransactionProductCode
import java.util.*

fun Transaction?.getFormattedDate(): String? {
    this?.creationDateTime?.let {
        val date = DateUtils.convertServerDateToLocalDate(it)
        date?.let { convertedDate ->
            val smsTime: Calendar = Calendar.getInstance()
            smsTime.timeInMillis = convertedDate.time
            val now: Calendar = Calendar.getInstance()
            return when {
                now.get(Calendar.DATE) === smsTime.get(Calendar.DATE) -> {
                    "Today " + DateUtils.dateToString(convertedDate, "MMM dd", false)
                }
                now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) === 1 -> {
                    "Yesterday " + DateUtils.dateToString(convertedDate, "MMM dd", false)
                }
                now.get(Calendar.YEAR) === smsTime.get(Calendar.YEAR) -> {
                    DateUtils.dateToString(convertedDate, "MMM dd YYYY", false)
                }
                else -> {
                    DateUtils.dateToString(convertedDate, "MMM dd YYYY", false)
                }
            }
        } ?: return "Others"
    } ?: return "Others"
}

fun Transaction?.getFormattedTime(): String? {
    this?.creationDateTime?.let {
        val date = DateUtils.convertServerDateToLocalDate(it)
        date?.let { convertedDate ->
            val smsTime: Calendar = Calendar.getInstance()
            smsTime.timeInMillis = convertedDate.time
            val dateTimeFormatString = "HH:mm a"
            return DateFormat.format(dateTimeFormatString, smsTime).toString()
        } ?: return null
    } ?: return null
}

fun Transaction?.getTransactionIcon(): Int {
    return when (this?.productCode) {
        TransactionProductCode.ATM_WITHDRAWAL.pCode -> R.drawable.pk_ic_atm_withdraw
        TransactionProductCode.POS_PURCHASE.pCode -> R.drawable.pk_ic_pos
        else -> R.drawable.pk_ic_pos
    }
}