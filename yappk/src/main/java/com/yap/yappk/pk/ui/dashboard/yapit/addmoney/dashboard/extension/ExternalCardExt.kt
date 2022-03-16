package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.digitify.core.utils.DateUtils
import com.yap.yappk.R
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CARD_INPUT_DATE_FORMAT
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CARD_OUTPUT_DATE_FORMAT
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CARD_TIME_ZONE
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CardType
import java.text.SimpleDateFormat
import java.util.*

fun ExternalCard.getCardLogoByType(context: Context, isPayCard: Boolean = false): Drawable? {
    return when (this.logo) {
        CardType.Visa.type ->
            if (isPayCard)
                ContextCompat.getDrawable(
                    context,
                    R.drawable.pk_ic_visa_logo
                )
            else
                ContextCompat.getDrawable(
                    context,
                    R.drawable.pk_ic_visa
                )
        CardType.Master.type ->
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_mc_logo
            )
        CardType.Amex.type ->
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_amex
            )
        CardType.Jcb.type ->
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_jcb
            )
        CardType.Dinners.type ->
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_diners_logo
            )
        CardType.Discover.type ->
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_discover_logo
            )
        else -> {
            ContextCompat.getDrawable(
                context,
                R.drawable.pk_ic_credit_card_default
            )
        }
    }
}

fun ExternalCard.getCardExpiry(context: Context): String? {
    return DateUtils.getFormattedLogDate(
        context,
        this.expiry ?: "",
        CARD_INPUT_DATE_FORMAT,
        CARD_OUTPUT_DATE_FORMAT,
        TimeZone.getTimeZone(CARD_TIME_ZONE)
    )
}

fun ExternalCard.isExpired(context: Context): Boolean {
    return this.getCardExpiry(context)?.let { date ->
        val dateFormat = SimpleDateFormat(CARD_OUTPUT_DATE_FORMAT)
        val expireDate = dateFormat.parse(date)
        val currentDate = Calendar.getInstance().time
        currentDate.after(expireDate)
    } ?: false
}

@ColorInt
fun ExternalCard.getCardColor(context: Context): Int {
    return try {
        Color.parseColor("#${this.color}")
    } catch (e: Exception) {
        TypedValue().also {
            context.theme.resolveAttribute(
                R.attr.colorPrimary,
                it,
                true
            )
        }.data
    }
}