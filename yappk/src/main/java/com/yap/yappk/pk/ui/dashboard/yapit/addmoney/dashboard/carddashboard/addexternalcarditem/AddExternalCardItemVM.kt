package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcarditem

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddExternalCardItemVM @Inject constructor(
    override val viewState: AddExternalCardItemState
) : BaseViewModel<IAddExternalCardItem.State>(), IAddExternalCardItem.ViewModel