package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.forgotpinupdatesuccess

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotCardPinUpdateSuccessVM @Inject constructor(
    override val viewState: ForgotCardPinUpdateSuccessState
) : BaseViewModel<IForgotCardPinUpdateSuccess.State>(), IForgotCardPinUpdateSuccess.ViewModel