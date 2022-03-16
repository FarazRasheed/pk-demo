package com.yap.yappk.pk.ui.onboarding.fullname

import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.SingleClickEvent
import com.yap.yappk.pk.utils.NameValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NameVerificationVM @Inject constructor(
    override val viewState: NameVerificationState,
    val nameValidator: NameValidator
) : BaseViewModel<INameVerification.State>(), INameVerification.ViewModel {
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    fun onFistNameTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValid.value =
            validateFullName(s.toString(), viewState.lastName.value ?: "")
    }

    fun onLastNameTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.isValid.value =
            validateFullName(viewState.firstName.value ?: "", s.toString())
    }

    fun validateFullName(firstName: String, lastName: String): Boolean {
        return nameValidator.firstNameValidate(firstName) && nameValidator.lastNameValidate(lastName)
    }
}