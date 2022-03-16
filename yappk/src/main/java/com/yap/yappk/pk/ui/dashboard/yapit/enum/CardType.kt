package com.yap.yappk.pk.ui.dashboard.yapit.enum

sealed class CardType(val type: String) {
    object Visa : CardType("Visa")
    object Master : CardType("Mastercard")
    object Jcb : CardType("JCB")
    object Dinners : CardType("Diners Club")
    object Amex : CardType("American Express")
    object Discover : CardType("Discover")
}