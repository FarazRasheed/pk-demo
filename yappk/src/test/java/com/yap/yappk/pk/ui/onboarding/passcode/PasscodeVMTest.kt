package com.yap.yappk.pk.ui.onboarding.passcode

import android.content.Context
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class PasscodeVMTest : BaseTestCase() {
    @Mock
    private lateinit var mMockContext: Context

    // Subject under test
    private lateinit var passcodeVM: PasscodeVM

    private lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
        passcodeVM = PasscodeVM(resourcesProviders)
    }

    @Test
    fun `test if passcode is valid should error return null`() {
        // Given
        val mobileNumberInput = "1212"
        passcodeVM.viewState.passcode.value = mobileNumberInput

        // When
        passcodeVM.handlePressOnCreatePasscode(mockk())

        // Then
        Assert.assertEquals(
            true,
            passcodeVM.viewState.passcodeError.getOrAwaitValue().isNullOrBlank()
        )
    }

    @Test
    fun `test if passcode is invalid should return error message`() {
        doReturn("Sequential numbers are not allowed")
            .`when`(mMockContext)
            .getString(any(Int::class.java))

        Mockito.`when`(resourcesProviders.getString(anyString()))
            .thenReturn("Sequential numbers are not allowed")

        // Given
        val mobileNumberInput = "1234"
        passcodeVM.viewState.passcode.value = mobileNumberInput

        // When
        passcodeVM.handlePressOnCreatePasscode(mockk())

        // Then
        Assert.assertEquals(
            "Sequential numbers are not allowed",
            passcodeVM.viewState.passcodeError.getOrAwaitValue()
        )

    }



}