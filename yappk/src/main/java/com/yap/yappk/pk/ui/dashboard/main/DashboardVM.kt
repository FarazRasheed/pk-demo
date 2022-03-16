package com.yap.yappk.pk.ui.dashboard.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.deeplink.IDeeplinkNavigator
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CardsUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.DebitCardUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    override val viewState: DashboardState,
    private val cardsUseCase: CardsUC,
    private val debitCardUseCase: DebitCardUC,
    override val deeplinkHandler: IDeeplinkNavigator
) : BaseViewModel<IDashboard.State>(), IDashboard.ViewModel {

    private val _debitCard: MutableLiveData<Card> = MutableLiveData()
    override val debitCard: LiveData<Card> = _debitCard

    override var animClosed = false

    private val _userCards: MutableLiveData<MutableList<Card>> = MutableLiveData()
    override val userCards: LiveData<MutableList<Card>> = _userCards

    private val _closeOverly: MutableLiveData<Boolean> = MutableLiveData()
    override val closeOverly: LiveData<Boolean> = _closeOverly

    override fun fetchDebitCard() {
        debitCardUseCase.executeUseCase(
            DebitCardUC.RequestValues(),
            object :
                UseCaseCallback<DebitCardUC.ResponseValue, DebitCardUC.ErrorValue> {
                override fun onSuccess(response: DebitCardUC.ResponseValue) {
                    _debitCard.value = response.card
                }

                override fun onError(error: DebitCardUC.ErrorValue) {
                    hideLoading()
                    _debitCard.value = null
                    //showAlertMessage(error.msg)
                }
            })

    }

    override fun fetchCards() {
        cardsUseCase.executeUseCase(
            CardsUC.RequestValues(),
            object :
                UseCaseCallback<CardsUC.ResponseValue, CardsUC.ErrorValue> {
                override fun onSuccess(response: CardsUC.ResponseValue) {
                    _userCards.value = response.cards as MutableList<Card>
                    hideLoading()
                }

                override fun onError(error: CardsUC.ErrorValue) {
                    hideLoading()
                    _userCards.value = null
                    //  showAlertMessage(error.msg)
                }
            })
    }

    override fun isOverlyShowing(): Boolean {
        return animClosed
    }

    override fun closeOverly() {
       _closeOverly.value = true
    }

    override fun onCreate() {
        fetchCards()
        fetchDebitCard()
    }

}