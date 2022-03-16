package com.yap.yappk.pk.ui.auth.main

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(override val viewState: AuthState) :
    BaseViewModel<IAuth.State>(), IAuth.ViewModel {
    override var mobileNo: String = ""
    override var countryCode: String = ""
    override var passcode: String = ""
}