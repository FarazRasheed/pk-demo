package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.accountqr

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountQrVM @Inject constructor(
    override val viewState: AccountQrState
) : BaseViewModel<IAccountQr.State>(), IAccountQr.ViewModel