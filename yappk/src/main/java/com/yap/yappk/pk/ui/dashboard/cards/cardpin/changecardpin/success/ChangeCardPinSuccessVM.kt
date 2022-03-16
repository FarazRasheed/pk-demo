package com.yap.yappk.pk.ui.dashboard.cards.cardpin.changecardpin.success

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeCardPinSuccessVM @Inject constructor(
    override val viewState: ChangeCardPinSuccessState
) :
    BaseViewModel<IChangeCardPinSuccess.State>(), IChangeCardPinSuccess.ViewModel