package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.main

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMoneyMainVM @Inject constructor(
    override val viewState: AddMoneyMainState
) : BaseViewModel<IAddMoneyMain.State>(), IAddMoneyMain.ViewModel