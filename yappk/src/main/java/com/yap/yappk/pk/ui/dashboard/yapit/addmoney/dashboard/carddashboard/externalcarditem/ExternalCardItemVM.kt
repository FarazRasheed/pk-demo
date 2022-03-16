package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarditem

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExternalCardItemVM @Inject constructor(
    override val viewState: ExternalCardItemState
) : BaseViewModel<IExternalCardItem.State>(), IExternalCardItem.ViewModel