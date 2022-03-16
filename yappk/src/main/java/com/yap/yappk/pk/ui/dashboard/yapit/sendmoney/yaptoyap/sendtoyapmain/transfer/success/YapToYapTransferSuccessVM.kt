package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.success

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YapToYapTransferSuccessVM @Inject constructor(
    override val viewState: YapToYapTransferSuccessState
) :
    BaseViewModel<IYapToYapTransferSuccess.State>(), IYapToYapTransferSuccess.ViewModel