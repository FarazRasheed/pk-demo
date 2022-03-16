package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.generateqr

import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenerateQrVM @Inject constructor(
    override val viewState: GenerateQrState,
    override val sessionManager: SessionManager
) : BaseViewModel<IGenerateQr.State>(), IGenerateQr.ViewModel
