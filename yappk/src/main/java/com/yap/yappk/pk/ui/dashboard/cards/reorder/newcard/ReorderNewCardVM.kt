package com.yap.yappk.pk.ui.dashboard.cards.reorder.newcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Address
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReorderNewCardVM @Inject constructor(
    override val viewState: ReorderNewCardState,
    private val cardsApi: CardsApi,
    private val transactionsApi: TransactionsApi

) :
    BaseViewModel<IReorderNewCard.State>(), IReorderNewCard.ViewModel {
    private val _reorderCardSuccess = MutableLiveData<Boolean>()
    override val reorderCardSuccess: LiveData<Boolean> = _reorderCardSuccess
    override var address: MutableLiveData<Address>? = MutableLiveData()

    override fun requestGetAddressForPhysicalCard() {
        launch {
            showLoading(true)
            val response =
                cardsApi.getPhysicalCardAddress()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        address?.value = response.data.data
                        viewState.cardAddressTitle?.value = address?.value?.address1
                        viewState.cardAddressSubTitle?.value = address?.value?.address2
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun getPhysicalCardFee() {
        launch {
            //showLoading(true)
            val response =
                transactionsApi.getPhysicalCardFee()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        viewState.reorderNewCardFee.value =
                            response.data.data?.totalFee.toString().toFormattedCurrency()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun getCardBalance(cardSerialNumber: String?) {
        launch {
            //showLoading(true)
            val response =
                cardsApi.getCardBalance(cardSerialNumber ?: "")
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        viewState.availableBalance.value =
                            response.data.data?.availableBalance.toFormattedCurrency()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    override fun reorderDebitCard(address: Address?) {
        launch {
            showLoading(true)
            val response =
                cardsApi.reorderDebitCard(address ?: Address())
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _reorderCardSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        _reorderCardSuccess.value = false
                        hideLoading()
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }
}