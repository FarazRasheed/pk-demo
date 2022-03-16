package com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.networking.microservices.cards.responsedtos.CardSchemeBenefit
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardorder.cardbenefits.cardschemebenefitlist.CardSchemeBenefitListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardSchemeBenefitsVM @Inject constructor(
    override val viewState: CardSchemeBenefitsState,
    private val cardApi: CardsApi,
    override val sessionManager: SessionManager
) : BaseViewModel<ICardSchemeBenefits.State>(), ICardSchemeBenefits.ViewModel {

    private val _cardScheme: MutableLiveData<SingleEvent<CardScheme>> = MutableLiveData()
    override val cardScheme: LiveData<SingleEvent<CardScheme>> = _cardScheme

    private val _cardSchemeBenefits: MutableLiveData<List<CardSchemeBenefit>> = MutableLiveData()
    override val cardSchemeBenefits: LiveData<List<CardSchemeBenefit>> = _cardSchemeBenefits

    private val _multiStateView: MutableLiveData<MultiState> = MutableLiveData()
    override val multiStateView: LiveData<MultiState> = _multiStateView
    override val cardSchemeBenefitAdapter: CardSchemeBenefitListAdapter =
        CardSchemeBenefitListAdapter(
            mutableListOf()
        )

    override fun setCardScheme(cardScheme: CardScheme?) {
        cardScheme?.let {
            _cardScheme.value = SingleEvent(it)
        }
    }

    override fun getCardSchemeBenefits(cardScheme: String) {
        launch {
            showLoading(onBackGround = true)
            val response = cardApi.getCardSchemeBenefits(cardScheme)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _cardSchemeBenefits.value = response.data.data
                        _multiStateView.value = MultiState.success()
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _cardSchemeBenefits.value = null
                        _multiStateView.value = MultiState.error()
                    }
                }
            }
        }
    }

    private val rvReloadListener = object : MultiStateLayout.OnReloadListener {
        override fun onReload(view: View) {
            getCardSchemeBenefits(cardScheme.value?.peekContent()?.schemeName ?: "")
        }
    }

    override fun getReloadListener(): MultiStateLayout.OnReloadListener {
        return rvReloadListener
    }
}