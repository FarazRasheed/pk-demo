package com.yap.yappk.pk.ui.kyc.cardorder.cardscheme

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.uikit.widget.multiStateView.MultiState
import com.yap.uikit.widget.multiStateView.MultiStateLayout
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardorder.cardscheme.cardschemelist.CardSchemeListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectCardSchemeVM @Inject constructor(
    override val viewState: SelectCardSchemeState,
    private val cardApi: CardsApi,
    private val sessionManager: SessionManager
) : BaseViewModel<ISelectCardScheme.State>(), ISelectCardScheme.ViewModel {
    private val _cardSchemes: MutableLiveData<List<CardScheme>> = MutableLiveData()
    override val cardSchemes: LiveData<List<CardScheme>> = _cardSchemes

    private val _multiStateView: MutableLiveData<MultiState> = MutableLiveData()
    override val multiStateView: LiveData<MultiState> = _multiStateView

    override val cardSchemeAdapter: CardSchemeListAdapter = CardSchemeListAdapter(
        arrayListOf(),
        sessionManager.userAccount.value?.currency?.code ?: "PKR"
    )

    override fun getCardSchemes() {
        launch {
            showLoading(onBackGround = true)
            val response = cardApi.getCardSchemes()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _cardSchemes.value = response.data.data
                        _multiStateView.value = MultiState.success()
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _cardSchemes.value = null
                        _multiStateView.value = MultiState.error()
                    }
                }
            }
        }
    }

    private val rvReloadListener = object : MultiStateLayout.OnReloadListener {
        override fun onReload(view: View) {
            getCardSchemes()
        }
    }

    override fun getReloadListener(): MultiStateLayout.OnReloadListener {
        return rvReloadListener
    }
}
