package com.yap.yappk.pk.ui.dashboard.yapit.enum

sealed class CardBgType(val color: String) {
    object Purple : CardBgType("Green")
    object Orange : CardBgType("Orange")
    object Blue : CardBgType("Blue")
    object Green : CardBgType("Green")
}