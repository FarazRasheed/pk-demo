package com.yap.yappk.pk.ui.dashboard.cards.cardpin

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.localization.error_passcode_same_digits
import com.yap.yappk.localization.error_passcode_sequence
import com.yap.yappk.localization.error_pin_code_not_same_digits
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PasscodeValidatorImplTest : BaseTestCase() {
    private lateinit var passcodeValidator: PasscodeValidatorImpl
    lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        resourcesProviders = mockk(relaxed = true)
        passcodeValidator = PasscodeValidatorImpl(resourcesProviders)

    }

    @Test
    fun `test if passcode has all same characters`() {
        val passcode = "1111"
        Assert.assertEquals(true, passcodeValidator.hasAllSameChars(passcode))
    }

    @Test
    fun `test if passcode has not all same characters`() {
        val passcode = "1212"
        Assert.assertEquals(false, passcodeValidator.hasAllSameChars(passcode))
    }

    @Test
    fun `test if passcode has sequence`() {
        val passcode = "1234"
        Assert.assertEquals(true, passcodeValidator.validatePasscodeSequence(passcode))
    }

    @Test
    fun `test if passcode has no sequence`() {
        val passcode = "1212"
        Assert.assertEquals(false, passcodeValidator.validatePasscodeSequence(passcode))
    }

    @Test
    fun `test if passcode is has sequential number error`() {
        every { resourcesProviders.getString(error_passcode_sequence) } returns "Sequential numbers are not allowed"

        val passcode = "1234"
        val passcodeResult = passcodeValidator.getPasscodeError(passcode)
        val expected = resourcesProviders.getString(error_passcode_sequence)

        Assert.assertEquals(expected, passcodeResult)

    }

    @Test
    fun `test if passcode is has same number error`() {
        every { resourcesProviders.getString(error_passcode_same_digits) } returns "Same numbers are not allowed"

        val passcode = "1111"
        val passcodeResult = passcodeValidator.getPasscodeError(passcode)
        val expected = resourcesProviders.getString(error_passcode_same_digits)

        Assert.assertEquals(expected, passcodeResult)

    }

    @Test
    fun `test if passcode is has not same with previous enter passcode error`() {
        every { resourcesProviders.getString(error_pin_code_not_same_digits) } returns "Passcode entered are not same as previously entered"

        val passcode = "1212"
        val confirmPasscode = "1234"
        val passcodeResult = passcodeValidator.getPasscodeError(passcode, confirmPasscode)
        val expected = resourcesProviders.getString(error_pin_code_not_same_digits)

        Assert.assertEquals(expected, passcodeResult)

    }
}
