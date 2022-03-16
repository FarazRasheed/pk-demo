package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.topupsuccess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.utils.toFormattedCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TopUpSuccessVM @Inject constructor(
    override val viewState: TopUpSuccessState,
    override val sessionManager: SessionManager,
    private val customersApi: CustomersApi
) : BaseViewModel<ITopUpSuccess.State>(), ITopUpSuccess.ViewModel {
    private val _availableBalance: MutableLiveData<String?> = MutableLiveData()
    override val availableBalance: LiveData<String?> = _availableBalance

    private val _topUpAmount: MutableLiveData<String?> = MutableLiveData()
    override val topUpAmount: LiveData<String?> = _topUpAmount

    private val _card: MutableLiveData<ExternalCard?> = MutableLiveData()
    override val card: LiveData<ExternalCard?> = _card

    override fun setTopUpAmount(amount: String?) {
        _topUpAmount.value = amount
    }

    override fun setCard(card: ExternalCard?) {
        _card.value = card
    }

    override fun getAccountBalance() {
        showLoading()
        launch {
            val response =
                customersApi.getAccountBalance()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _availableBalance.value =
                            response.data.data?.currentBalance.toString().toFormattedCurrency()
                    }
                    is ApiResponse.Error -> {
                        showToast(response.error.message)
                    }
                }
                hideLoading()
            }
        }
    }
}