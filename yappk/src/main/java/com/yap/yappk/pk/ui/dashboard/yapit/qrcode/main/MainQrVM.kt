package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.main

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainQrVM @Inject constructor(override val viewState: MainQrState) :
    BaseViewModel<IMainQr.State>(), IMainQr.ViewModel