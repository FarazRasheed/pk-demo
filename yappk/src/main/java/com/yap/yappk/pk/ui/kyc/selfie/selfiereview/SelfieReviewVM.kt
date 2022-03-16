package com.yap.yappk.pk.ui.kyc.selfie.selfiereview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelfieReviewVM @Inject constructor(
    override val viewState: SelfieReviewState,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) : BaseViewModel<ISelfieReview.State>(), ISelfieReview.ViewModel {
    private val _isSelfieUploaded: MutableLiveData<Boolean> = MutableLiveData()
    override val isSelfieUploaded: LiveData<Boolean> = _isSelfieUploaded
    private val _openCardNameConfirmation = MutableLiveData<SingleEvent<Int>>()
    override val openCardNameConfirmation: LiveData<SingleEvent<Int>> get() = _openCardNameConfirmation

    override fun uploadSelfie(selfiePath: String) {
        launch {
            showLoading(true)
            val response = customersApi.uploadSelfie(selfiePath)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isSelfieUploaded.value = false
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
                    _isSelfieUploaded.value = true
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
                    _isSelfieUploaded.value = false
                }
            }
        }
    }

    override fun openCardSchemeSelectionScreen() {
        _openCardNameConfirmation.value =
            SingleEvent(R.id.action_selfieReviewFragment_to_selectCardSchemeFragment)
    }
}