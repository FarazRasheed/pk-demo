package com.yap.yappk.pk.utils

import javax.inject.Inject

class PersonNameValidator @Inject constructor() : NameValidator {

    override fun firstNameValidate(firstName: String): Boolean {
        val expression =
            "^[a-zA-Z]{1}[a-zA-Z ]{1,100}\$"
        return firstName.matches(expression.toRegex())
    }

    override fun lastNameValidate(lastName: String): Boolean {
        val expression =
            "^[a-zA-Z ]{1,100}\$"
        return lastName.matches(expression.toRegex())
    }

}

interface NameValidator {
    fun firstNameValidate(firstName: String): Boolean
    fun lastNameValidate(lastName: String): Boolean
}