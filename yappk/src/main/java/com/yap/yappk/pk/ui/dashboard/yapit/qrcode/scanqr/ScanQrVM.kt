package com.yap.yappk.pk.ui.dashboard.yapit.qrcode.scanqr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanQrVM @Inject constructor(
    override val viewState: ScanQrState,
    private val customersApi: CustomersApi,
    override val sessionManager: SessionManager
) : BaseViewModel<IScanQr.State>(), IScanQr.ViewModel {

    private val _userInfo: MutableLiveData<IBeneficiary?> = MutableLiveData()
    override val userInfo: LiveData<IBeneficiary?> = _userInfo

    override fun getQrCodeUserInfo(qrCode: String) {
        launch {
            showLoading(true)
            val response =
                customersApi.getUserFromQrCode(uuid = qrCode)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _userInfo.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _userInfo.value = null
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

}