package com.yap.yappk.pk.ui.dashboard.cards.cardpin

interface IPasscodeValidator {
    fun validateConfirmPasscode(passcode: String?, confirmPasscode: String?): String?
    fun validatePasscode(passcode: String?): String?
}