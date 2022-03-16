package com.yap.yappk.pk.ui.dashboard.cards.cardpin.cardpinenter

import android.content.Context
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter.CardPinEnterState
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter.CardPinEnterVM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CardPinEnterVMTest : BaseTestCase() {
    @Mock
    private lateinit var mMockContext: Context

    // Subject under test
    private lateinit var cardPinEnterVM: CardPinEnterVM

    // Mock resource provider
    private lateinit var resourcesProviders: ResourcesProviders

    // view state added
    private val viewState = CardPinEnterState()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        cardPinEnterVM = CardPinEnterVM(viewState, resourcesProviders)
    }

    @Test
    fun `test if pin code is valid should error return null`() {
        // Given
        val mobileNumberInput = "1212"
        cardPinEnterVM.viewState.pin.value = mobileNumberInput

        // When
        cardPinEnterVM.handlePressOnNext()

        // Then
        Assert.assertEquals(
            true,
            cardPinEnterVM.viewState.pinError.getOrAwaitValue().isNullOrBlank()
        )
    }

    @Test
    fun `test if passcode is invalid should return error message`() {
        Mockito.doReturn("Sequential numbers are not allowed")
            .`when`(mMockContext)
            .getString(ArgumentMatchers.any(Int::class.java))

        Mockito.`when`(resourcesProviders.getString(ArgumentMatchers.anyString()))
            .thenReturn("Sequential numbers are not allowed")

        // Given
        val pinCodeInput = "1234"
        cardPinEnterVM.viewState.pin.value = pinCodeInput

        // When
        cardPinEnterVM.handlePressOnNext()

        // Then
        Assert.assertEquals(
            "Sequential numbers are not allowed",
            cardPinEnterVM.viewState.pinError.getOrAwaitValue()
        )

    }
}