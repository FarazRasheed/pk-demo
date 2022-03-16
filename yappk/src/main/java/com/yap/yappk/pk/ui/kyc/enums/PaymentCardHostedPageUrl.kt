package com.yap.yappk.pk.ui.kyc.enums

sealed class PaymentCardHostedPageUrl(val url: String) {
    object DEV :
        PaymentCardHostedPageUrl("https://pk-dev-hci.yap.co/YAP_PK_BANK_ALFALAH/HostedSessionIntegration.html")

    object STG :
        PaymentCardHostedPageUrl("https://pk-stg-hci.yap.co/YAP_PK_BANK_ALFALAH/HostedSessionIntegration.html")

    object QA :
        PaymentCardHostedPageUrl("https://pk-qa-hci.yap.co/YAP_PK_BANK_ALFALAH/HostedSessionIntegration.html")
}