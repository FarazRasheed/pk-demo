package com.yap.yappk.pk.utils

import java.text.DecimalFormat

fun String?.maskIbanNumber(): String {
    return formatIbanString(this)?.let { formattedIban ->
        val numberToMasked =
            formattedIban.substring(formattedIban.length - 7, formattedIban.length)
        return formattedIban.replace(numberToMasked, "*** ***")
    } ?: return ""
}

private fun formatIbanString(iban: String?): String? {
    iban?.let {
        val sb = StringBuilder()
        for (i in iban.indices) {
            if (i % 4 == 0 && i > 0) {
                sb.append(" ")
            }
            sb.append(iban[i])
        }
        return sb.toString()
    } ?: return null

}

fun String?.formatCardString(): String? {
    this?.let {
        val sb = StringBuilder()
        for (i in it.indices) {
            if (i % 4 == 0 && i > 0) {
                sb.append(" ")
            }
            sb.append(it[i])
        }
        return sb.toString()
    } ?: return null

}
fun String?.toFormattedCurrency(
    showCurrency: Boolean = true,
    currency: String? = "PKR",
    withComma: Boolean = true
): String {
    return try {
        if (this?.isNotBlank() == true) {
            val formattedAmount = getDecimalFormatUpTo(
                selectedCurrencyDecimal = getDecimalFromValue(this),
                amount = this,
                withComma = withComma
            )
            if (formattedAmount.isNotBlank()) {
                if (showCurrency)
                    "$currency $formattedAmount" else formattedAmount
            } else {
                ""
            }
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

fun getDecimalFormatUpTo(
    selectedCurrencyDecimal: Int = 2,
    amount: String,
    withComma: Boolean = true
): String {
    return try {
        val amountInDouble = java.lang.Double.parseDouble(amount)
        return when (selectedCurrencyDecimal) {
            0 -> {
                if (withComma)
                    DecimalFormat("###,###,##0").format(amountInDouble)
                else
                    DecimalFormat("########0").format(amountInDouble)
            }
            1 -> {
                if (withComma)
                    DecimalFormat("###,###,##0.0").format(amountInDouble)
                else
                    DecimalFormat("########0.0").format(amountInDouble)
            }
            2 -> {
                if (withComma)
                    DecimalFormat("###,###,##0.00").format(amountInDouble)
                else
                    DecimalFormat("########0.00").format(amountInDouble)
            }
            3 -> {
                if (withComma)
                    DecimalFormat("###,###,##0.000").format(amountInDouble)
                else
                    DecimalFormat("########0.000").format(amountInDouble)
            }
            4 -> {
                if (withComma)
                    DecimalFormat("###,###,##0.0000").format(amountInDouble)
                else
                    DecimalFormat("########0.0000").format(amountInDouble)
            }
            6 -> {
                if (withComma)
                    DecimalFormat("###,###,##0.000000").format(amountInDouble)
                else
                    DecimalFormat("########0.000000").format(amountInDouble)
            }
            else -> {
                if (withComma)
                    DecimalFormat("###,###,##0.00").format(
                        amountInDouble
                    )
                else
                    DecimalFormat("########0.00").format(
                        amountInDouble
                    )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun getDecimalFromValue(amount: String): Int {
    val splitAmount = amount.split(".")
    return if (splitAmount.size > 1) {
        if (splitAmount[1].length < 2) {
            2
        } else {
            splitAmount[1].length
        }
    } else {
        2
    }
}