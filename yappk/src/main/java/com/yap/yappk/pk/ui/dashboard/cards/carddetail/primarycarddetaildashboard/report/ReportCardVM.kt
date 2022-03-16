package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.networking.microservices.messages.MessagesApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReportCardVM @Inject constructor(
    override val viewState: ReportCardState, private val messagesApi: MessagesApi, private val cardsApi: CardsApi
) : BaseViewModel<IReportCard.State>(), IReportCard.ViewModel {
    private val _reportCardSuccess = MutableLiveData<Boolean>()
    override val reportCardSuccess: LiveData<Boolean> = _reportCardSuccess
    private val _openReorderFlow = MutableLiveData<SingleEvent<Int>>()
    override val openReorderFlow: LiveData<SingleEvent<Int>> = _openReorderFlow
    override var HOT_LIST_REASON: Int = 2

    override fun toggleReportLostAndDamageCardOptions(toggle: Boolean) {
        viewState.isCardDamageOption.value = toggle
    }

    override fun getHelpDeskNumber() {
        launch {
            val response =
                messagesApi.getHelpDesk()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        viewState.helpDeskNumber?.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                    }
                }
            }
        }
    }

    override fun requestConfirmBlockCard(card: Card?) {
        launch {
            showLoading(true)
            val response =
                cardsApi.reportAndBlockCard(
                    cardSerialNumber = card?.cardSerialNumber ?: "",
                    hotListReason = HOT_LIST_REASON.toString()
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _reportCardSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        _reportCardSuccess.value = false
                        showAlertMessage(response.error.message)
                        hideLoading()
                    }
                }
            }
        }
    }

}
