package com.yap.yappk.pk.ui.onboarding.fullname

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.pk.utils.NameValidator
import com.yap.yappk.pk.utils.PersonNameValidator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NameVerificationVMTest : BaseTestCase() {

    lateinit var nameValidator: NameValidator
    lateinit var nameVerificationState: NameVerificationState

    // System under test
    lateinit var nameVerificationVM: NameVerificationVM

    @Before
    fun setUp() {
        nameValidator = PersonNameValidator()
        nameVerificationState = NameVerificationState()
        nameVerificationVM = NameVerificationVM(nameVerificationState, nameValidator)
    }

    @Test
    fun `test if given first name and last name is valid should return true`() {
        // Given
        val firstNameInput = "Muhammad Mughees"
        val lastNameInput = "Akram"
        nameVerificationVM.viewState.firstName.value = firstNameInput
        nameVerificationVM.viewState.lastName.value = lastNameInput

        // When
        nameVerificationVM.onFistNameTextChanged("Mughees", 0, 0, 0)
        nameVerificationVM.viewState.firstName.value = "Mughees"
        nameVerificationVM.onLastNameTextChanged(lastNameInput, 0, 0, 0)

        // Then
        Assert.assertEquals(true, nameVerificationVM.viewState.isValid.getOrAwaitValue())
    }

    @Test
    fun `test if given first name and last name is in-valid should return false`() {
        // Given
        val firstNameInput = "Muhammad Mughees"
        val lastNameInput = "Akram"
        nameVerificationVM.viewState.firstName.value = firstNameInput
        nameVerificationVM.viewState.lastName.value = lastNameInput

        // When
        nameVerificationVM.onFistNameTextChanged("Muhammad Mughee1", 0, 0, 0)
        nameVerificationVM.viewState.firstName.value = "Muhammad Mughee1"
        nameVerificationVM.onLastNameTextChanged("Akram1", 0, 0, 0)
        nameVerificationVM.viewState.lastName.value = "Akram1"

        // Then
        Assert.assertEquals(false, nameVerificationVM.viewState.isValid.getOrAwaitValue())
    }
}