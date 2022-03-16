package com.yap.yappk.pk.ui.dashboard.cards.carditem.addcard

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCardVM @Inject constructor(
    override val viewState: AddCardState
) : BaseViewModel<IAddCard.State>(), IAddCard.ViewModel