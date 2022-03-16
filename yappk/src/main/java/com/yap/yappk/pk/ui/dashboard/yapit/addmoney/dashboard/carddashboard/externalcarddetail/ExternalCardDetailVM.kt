package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExternalCardDetailVM @Inject constructor(
    override val viewState: ExternalCardDetailState,
    private val customersApi: CustomersApi,
) : BaseViewModel<IExternalCardDetail.State>(), IExternalCardDetail.ViewModel {

    private val _card: MutableLiveData<ExternalCard?> = MutableLiveData()
    override val card: LiveData<ExternalCard?> = _card

    private val _isCardDeleted: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardDeleted: LiveData<Boolean> = _isCardDeleted

    override fun setCard(card: ExternalCard?) {
        _card.value = card
    }

    override fun deleteExternalCard(cardId: String) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.deleteExternalCard(cardId = cardId)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _isCardDeleted.value = true
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        _isCardDeleted.value = false
                        hideLoading()
                    }
                }
            }
        }
    }
}