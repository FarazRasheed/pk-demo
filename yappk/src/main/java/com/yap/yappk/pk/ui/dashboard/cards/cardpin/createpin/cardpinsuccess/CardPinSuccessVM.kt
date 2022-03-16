package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinsuccess

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPinSuccessVM @Inject constructor(override val viewState: CardPinSuccessState) :
    BaseViewModel<ICardPinSuccess.State>(), ICardPinSuccess.ViewModel