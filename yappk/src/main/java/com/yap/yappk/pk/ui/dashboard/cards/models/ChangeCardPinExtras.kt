package com.yap.yappk.pk.ui.dashboard.cards.models

import android.os.Parcelable
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeCardPinExtras(
    var oldCardPin: String? = null,
    var screenType: ChangePinScreenType? = null,
    var cardConfirmPin: String? = null
) : Parcelable