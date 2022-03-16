package com.yap.yappk.pk.ui.auth.forgotpasscode.newpasscodesuccess

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPasscodeSuccessVM @Inject constructor(
    override val viewState: NewPasscodeSuccessState
) : BaseViewModel<INewPasscodeSuccess.State>(), INewPasscodeSuccess.ViewModel {
}