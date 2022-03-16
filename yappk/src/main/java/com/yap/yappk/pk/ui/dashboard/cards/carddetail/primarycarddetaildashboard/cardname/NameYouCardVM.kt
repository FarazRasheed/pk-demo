package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardname

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NameYouCardVM @Inject constructor(
    override val viewState: NameYouCardState,
    private val cardsApi: CardsApi
) :
    BaseViewModel<INameYouCard.State>(), INameYouCard.ViewModel {

    private val _setCardNameSuccess = MutableLiveData<Boolean>()
    override val setCardNameSuccess: LiveData<Boolean> = _setCardNameSuccess

    private val _navigateToCardDashboard = MutableLiveData<SingleEvent<Int>>()
    override val navigateToCardDashboard: LiveData<SingleEvent<Int>> = _navigateToCardDashboard

    fun onCardNameTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.valid.value = s.toString().isNotEmpty()
    }

    override fun setCardNameRequest(cardName: String, cardSerialNumber: String) {
        launch {
            showLoading(true)
            val response = cardsApi.setCardName(cardName, cardSerialNumber)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _setCardNameSuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _setCardNameSuccess.value = false
                    }
                }
            }
        }
    }

    override fun navigateToCardDashboard() {
        _navigateToCardDashboard.value =
            SingleEvent(R.id.primaryCardDetailFragment)
    }
}