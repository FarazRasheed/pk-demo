package com.yap.yappk.pk.ui.dashboard.yapit.enum

sealed class CardHostedFlavour(val flavour: String) {
    object DEV : CardHostedFlavour("dev")
    object STG : CardHostedFlavour("stg")
    object QA : CardHostedFlavour("qa")
}