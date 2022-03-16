package com.yap.yappk.pk.ui.dashboard.cards.carddetail.cardlimit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardLimitVM @Inject constructor(
    override val viewState: CardLimitState,
    private val cardsApi: CardsApi
) : BaseViewModel<ICardLimit.State>(), ICardLimit.ViewModel {

    private val _isAtmConfigUpdated = MutableLiveData<Boolean>()
    override val isAtmConfigUpdated: LiveData<Boolean> get() = _isAtmConfigUpdated

    private val _isRetailConfigUpdated = MutableLiveData<Boolean>()
    override val isRetailConfigUpdated: LiveData<Boolean> get() = _isRetailConfigUpdated

    override fun updateAtmConfig(cardSerialNumber: String) {
        launch {
            showLoading(true)
            val response = cardsApi.configAllowAtm(cardSerialNumber)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isAtmConfigUpdated.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isAtmConfigUpdated.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun updateRetailConfig(cardSerialNumber: String) {
        launch {
            showLoading(true)
            val response = cardsApi.configRetailPayment(cardSerialNumber)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _isRetailConfigUpdated.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isRetailConfigUpdated.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

}