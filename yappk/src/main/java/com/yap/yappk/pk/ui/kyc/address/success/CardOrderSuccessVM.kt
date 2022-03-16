package com.yap.yappk.pk.ui.kyc.address.success

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardOrderSuccessVM @Inject constructor(override val viewState: CardOrderSuccessState) :
    BaseViewModel<ICardOrderSuccess.State>(), ICardOrderSuccess.ViewModel