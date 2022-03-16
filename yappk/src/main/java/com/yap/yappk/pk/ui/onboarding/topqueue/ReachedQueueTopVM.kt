package com.yap.yappk.pk.ui.onboarding.topqueue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CompleteVerificationRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReachedQueueTopVM @Inject constructor(
    override val viewState: ReachedQueueTopState,
    private val sessionManager: SessionManager,
    private val customersApi: CustomersApi
) : BaseViewModel<IReachedQueueTop.State>(), IReachedQueueTop.ViewModel {
    private val _isCompleteVerification: MutableLiveData<Boolean> = MutableLiveData()
    override val isCompleteVerification: LiveData<Boolean> = _isCompleteVerification
    private val _openCompleteVerification = MutableLiveData<SingleEvent<AccountInfo?>>()
    override val openCompleteVerification: LiveData<SingleEvent<AccountInfo?>> get() = _openCompleteVerification
    override fun onCreate() {
        super.onCreate()
        viewState.firstName.set(sessionManager.userAccount.value?.currentCustomer?.firstName)
        viewState.countryCode.set(sessionManager.userAccount.value?.currentCustomer?.countryCode)
        viewState.mobileNo.set(sessionManager.userAccount.value?.currentCustomer?.mobileNo)
    }

    override fun completeVerification(completeVerificationRequest: CompleteVerificationRequest) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.completeVerification(completeVerificationRequest)
            launch(Dispatcher.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        _isCompleteVerification.value = false
                        hideLoading()
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }


    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _isCompleteVerification.value = true
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
                    _isCompleteVerification.value = false
                }
            }
        }
    }

    override fun getCompleteVerificationRequest(): CompleteVerificationRequest {
        return CompleteVerificationRequest(
            mobileNo = viewState.mobileNo.get(),
            countryCode = viewState.countryCode.get()
        )
    }

    override fun openCompleteVerificationScreen() {
        _openCompleteVerification.value = SingleEvent(sessionManager.userAccount.value)
    }
}