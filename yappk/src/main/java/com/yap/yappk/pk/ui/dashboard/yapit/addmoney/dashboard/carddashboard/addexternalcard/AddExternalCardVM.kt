package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CardHostedFlavour
import com.yap.yappk.pk.ui.dashboard.yapit.enum.CardHostedPageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AddExternalCardVM @Inject constructor(
    override val viewState: AddExternalCardState,
    override val pkBuildConfigurations: PKBuildConfigurations,
    private val customersApi: CustomersApi,
) : BaseViewModel<IAddExternalCard.State>(), IAddExternalCard.ViewModel {
    private val _card: MutableLiveData<ExternalCard> = MutableLiveData()
    override val card: LiveData<ExternalCard> = _card

    override fun getAddCardPageUrl(buildFlavour: String): String {
        return when (buildFlavour) {
            CardHostedFlavour.DEV.flavour -> {
                CardHostedPageUrl.DEV.url
            }
            CardHostedFlavour.QA.flavour -> {
                CardHostedPageUrl.QA.url
            }
            CardHostedFlavour.STG.flavour -> {
                CardHostedPageUrl.STG.url
            }
            else -> ""
        }
    }

    override fun addExternalCard(
        sessionId: String,
        cardAlias: String,
        cardColor: String,
        cardNumber: String
    ) {
        launch {
            showLoading(onBackGround = true)
            val response = customersApi.addExternalCard(
                sessionId = sessionId,
                cardAlias = cardAlias,
                cardColor = cardColor,
                cardNumber = cardNumber
            )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _card.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _card.value = null
                    }
                }
            }
        }
    }
}