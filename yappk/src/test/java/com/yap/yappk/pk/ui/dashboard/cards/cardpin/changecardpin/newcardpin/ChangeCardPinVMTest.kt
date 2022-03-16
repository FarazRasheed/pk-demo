package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.newcardpin

import android.content.Context
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.PasscodeValidatorImpl
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.ChangePinScreenType
import com.yap.yappk.pk.ui.dashboard.cards.models.ChangeCardPinExtras
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ChangeCardPinVMTest : BaseTestCase() {

    //Subject under test
    private lateinit var changeCardPinVM: ChangeCardPinVM

    //mock resources provider
    private lateinit var resourcesProviders: ResourcesProviders
    private lateinit var changeCardPinExtras: ChangeCardPinExtras

    //view state added
    private val viewState = ChangeCardPinState()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        changeCardPinExtras = ChangeCardPinExtras(screenType = ChangePinScreenType.SCREEN_TYPE_CHANGE_PIN)
        changeCardPinVM = ChangeCardPinVM(
            viewState = viewState,
            cardsApi = mockk(),
            passCodeValidator = PasscodeValidatorImpl(resourcesProviders)
        )
    }

    @Test
    fun `test if passcode is valid for change card pin should error return null`() {
        Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
            .thenReturn("Sequential numbers are not allowed")

        val passcodeDigits = "1911"
        changeCardPinVM.viewState.screenType.value = ChangePinScreenType.SCREEN_TYPE_CHANGE_PIN.name
        changeCardPinVM.viewState.pin.value = passcodeDigits

        changeCardPinVM.checkPasscodeValidation()

        Assert.assertEquals(true,viewState.pinError.getOrAwaitValue().isNullOrBlank())
    }

    @Test
    fun `test if passcode is valid for confirm card pin should error return null`() {
        Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
            .thenReturn("Sequential numbers are not allowed")

        val passcodeDigits = "1911"
        val confirmPasscode = "1911"
        changeCardPinExtras.also {
            it.cardConfirmPin = confirmPasscode
        }
        viewState.screenType.value = ChangePinScreenType.SCREEN_TYPE_CHANGE_CONFIRM_PIN.name
        viewState.changeCardPinModel.value = changeCardPinExtras
        viewState.pin.value = passcodeDigits

        changeCardPinVM.checkPasscodeValidation()

        Assert.assertEquals(true,viewState.pinError.getOrAwaitValue().isNullOrBlank())
    }
}
