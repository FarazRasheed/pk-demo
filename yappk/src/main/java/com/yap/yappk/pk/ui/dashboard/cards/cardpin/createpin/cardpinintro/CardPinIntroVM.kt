package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinintro

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPinIntroVM @Inject constructor(override val viewState: CardPinIntroState) :
    BaseViewModel<ICardPinIntro.State>(), ICardPinIntro.ViewModel