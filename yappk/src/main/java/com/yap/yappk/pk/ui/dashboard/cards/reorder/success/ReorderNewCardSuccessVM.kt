package com.yap.yappk.pk.ui.dashboard.cards.reorder.success

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReorderNewCardSuccessVM @Inject constructor(
    override val viewState: ReorderNewCardSuccessState
) :
    BaseViewModel<IReorderNewCardSuccess.State>(), IReorderNewCardSuccess.ViewModel