package com.yap.yappk.pk.ui.kyc.cardorder.cardpaymentdashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter.ExternalCardPagerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardPaymentDashboardVM @Inject constructor(
    override val viewState: CardPaymentDashboardState,
    private val customersApi: CustomersApi,
    override val sessionManager: SessionManager,
) : BaseViewModel<ICardPaymentDashboard.State>(), ICardPaymentDashboard.ViewModel {

    private val _externalCards: MutableLiveData<List<ExternalCard>> = MutableLiveData()
    override val externalCards: LiveData<List<ExternalCard>> = _externalCards

    private val _card: MutableLiveData<SingleEvent<ExternalCard?>> = MutableLiveData()
    override val card: LiveData<SingleEvent<ExternalCard?>> = _card

    override var cardsAdapter: ExternalCardPagerAdapter? = null
    override var pagerCurrentPosition: Int = -1

    override fun getExternalCards() {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.getExternalCards()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _externalCards.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _externalCards.value = null
                    }
                }
            }
        }
    }

    override fun setCard(card: ExternalCard?) {
        _card.value = SingleEvent(card)
    }
}