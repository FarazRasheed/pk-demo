package com.yap.yappk.pk.ui.dashboard.cards.cardpin.cardconfirmpin

import android.content.Context
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin.CardConfirmPinState
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardconfirmpin.CardConfirmPinVM
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
class CardConfirmPinVMTest : BaseTestCase() {
    @Mock
    private lateinit var mMockContext: Context

    // Subject under test
    private lateinit var cardConfirmPinVM: CardConfirmPinVM

    // Mock resource provider
    private lateinit var resourcesProviders: ResourcesProviders

    // view state added
    private val viewState = CardConfirmPinState()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        cardConfirmPinVM = CardConfirmPinVM(viewState, resourcesProviders, mockk())
    }

    @Test
    fun `test if pin code is valid should error return null`() {
        // Given
        val mobileNumberInput = "1212"
        cardConfirmPinVM.viewState.newPin.value = mobileNumberInput
        cardConfirmPinVM.confirmPin = mobileNumberInput

        // When
        cardConfirmPinVM.handlePressOnCreate()

        // Then
        Assert.assertEquals(
            true,
            cardConfirmPinVM.viewState.pinError.getOrAwaitValue().isNullOrBlank()
        )
    }

    @Test
    fun `test if pin code is invalid should return error message`() {
        Mockito.doReturn("This doesn't match the previously entered PIN")
            .`when`(mMockContext)
            .getString(ArgumentMatchers.any(Int::class.java))

        Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
            .thenReturn("This doesn't match the previously entered PIN")

        // Given
        val pinCodeInput = "1234"
        cardConfirmPinVM.viewState.newPin.value = pinCodeInput
        cardConfirmPinVM.confirmPin = "1546"

        // When
        cardConfirmPinVM.handlePressOnCreate()

        // Then
        Assert.assertEquals(
            "This doesn't match the previously entered PIN",
            cardConfirmPinVM.viewState.pinError.getOrAwaitValue()
        )

    }
}