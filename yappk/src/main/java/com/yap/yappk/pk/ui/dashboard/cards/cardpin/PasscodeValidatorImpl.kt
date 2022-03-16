package com.yap.yappk.pk.ui.dashboard.cards.cardpin

import com.yap.uikit.utils.extensions.hasAllSameChars
import com.yap.uikit.utils.extensions.isSequenced
import com.yap.yappk.localization.error_passcode_same_digits
import com.yap.yappk.localization.error_passcode_sequence
import com.yap.yappk.localization.error_pin_code_not_same_digits
import com.yap.yappk.pk.di.ResourcesProviders

class PasscodeValidatorImpl constructor(private val resourcesProviders: ResourcesProviders) :
    IPasscodeValidator {

    override fun validatePasscode(passcode: String?): String {
        return if (validatePasscodeSequence(passcode) || hasAllSameChars(passcode)) getPasscodeError(passcode) else ""
    }

    override fun validateConfirmPasscode(
        passcode: String?,
        confirmPasscode: String?
    ): String {
        return if (passcode == confirmPasscode) "" else getPasscodeError(passcode, confirmPasscode)
    }

    fun validatePasscodeSequence(passcode: String?): Boolean {
        return passcode?.isSequenced() ?: false
    }

    fun hasAllSameChars(passcode: String?): Boolean {
        return passcode?.hasAllSameChars() ?: false
    }

    fun getPasscodeError(passcode: String?, confirmPasscode: String? = ""): String {
        return when {
            hasAllSameChars(passcode) && confirmPasscode.isNullOrBlank() -> resourcesProviders.getString(
                keyID = error_passcode_same_digits
            )
            validatePasscodeSequence(passcode) && confirmPasscode.isNullOrBlank() -> resourcesProviders.getString(
                keyID = error_passcode_sequence
            )
            passcode != confirmPasscode -> resourcesProviders.getString(keyID = error_pin_code_not_same_digits)
            else -> ""
        }
    }
}