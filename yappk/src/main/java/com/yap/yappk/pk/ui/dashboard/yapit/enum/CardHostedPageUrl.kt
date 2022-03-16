package com.yap.yappk.pk.ui.dashboard.yapit.enum

sealed class CardHostedPageUrl(val url: String) {
    object DEV : CardHostedPageUrl("https://pk-dev-hci.yap.co/admin-web/HostedSessionIntegration.html")
    object STG : CardHostedPageUrl("https://pk-stg-hci.yap.co/admin-web/HostedSessionIntegration.html")
    object QA : CardHostedPageUrl("https://pk-qa-hci.yap.co/admin-web/HostedSessionIntegration.html")
}