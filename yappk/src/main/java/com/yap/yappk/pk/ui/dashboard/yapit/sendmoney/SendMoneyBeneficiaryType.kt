package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney

sealed class SendMoneyBeneficiaryType(var type: String) {
    object IBFTBeneficiary : SendMoneyBeneficiaryType("IBFT")
}
