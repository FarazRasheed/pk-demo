package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.cardtransactionfilter

import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.R
import com.yap.yappk.localization.screen_card_transaction_filter_display_text_balance
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.transactions.TransactionsApi
import com.yap.yappk.networking.microservices.transactions.responsedtos.FilterAmount
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.enums.FilterTransactionType
import com.yap.yappk.pk.ui.dashboard.cards.models.FilterExtras
import com.yap.yappk.pk.utils.rangebar.OnRangeChangedListener
import com.yap.yappk.pk.utils.rangebar.RangeSeekBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardTransactionFilterVM @Inject constructor(
    override val viewState: CardTransactionFilterState,
    private val transactionsApi: TransactionsApi,
    override val sessionManager: SessionManager,
    private val resourcesProvider: ResourcesProviders,
) : BaseViewModel<ICardTransactionFilter.State>(), ICardTransactionFilter.ViewModel {

    private val _filterAmount: MutableLiveData<FilterAmount> = MutableLiveData()
    override val filterAmount: LiveData<FilterAmount> = _filterAmount

    override var filterData: FilterExtras? = null
    fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
        when (button.id) {
            R.id.cbRetails -> viewState.isRetailPayment.value = isChecked
            R.id.cbAtm -> viewState.isAtmWithdrawAllow.value = isChecked
        }
    }

    private val rangeChangedListener = object : OnRangeChangedListener {
        override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) = Unit

        override fun onRangeChanged(
            rangeSeekbar: RangeSeekBar?,
            leftValue: Float,
            rightValue: Float,
            isFromUser: Boolean
        ) {
            viewState.maxRange.value = rightValue.toInt().toString()
            viewState.minRange.value = leftValue.toInt().toString()
        }

        override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) = Unit
    }

    override fun fetchFilterAmount() {
        launch {
            showLoading(true)
            val response = transactionsApi.getMinMaxFilterAmount()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _filterAmount.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _filterAmount.value = null
                    }
                }
            }
        }
    }

    override fun getRangeTitle(): String {
        return resourcesProvider.getString(
            screen_card_transaction_filter_display_text_balance,
            sessionManager.userAccount.value?.currency?.code ?: ""
        )
    }

    override fun setFilterData() {
        filterData = FilterExtras()
        setRangeData()
        setTnxType()
        filterData?.isFilterSet = true
    }

    private fun setTnxType() {
        when {
            ((viewState.isAtmWithdrawAllow.value == true && viewState.isRetailPayment.value == true) ||
                    (viewState.isAtmWithdrawAllow.value == false && viewState.isRetailPayment.value == false)) -> Unit
            (viewState.isAtmWithdrawAllow.value == true) -> {
                filterData?.txnType = FilterTransactionType.ATM_WITHDRAW.name
                filterData?.filterCount = filterData?.filterCount?.plus(1) ?: 0
            }
            else -> {
                filterData?.txnType = FilterTransactionType.POS.name
                filterData?.filterCount = filterData?.filterCount?.plus(1) ?: 0
            }
        }
    }

    private fun setRangeData() {
        if (filterAmount.value?.maxAmount?.toFloat() != viewState.maxRange.value?.toFloat() ||
            filterAmount.value?.minAmount?.toFloat() != viewState.minRange.value?.toFloat()
        ) {
            filterData?.maxRange = viewState.maxRange.value
            filterData?.minRange = viewState.minRange.value
            filterData?.filterCount = filterData?.filterCount?.plus(1) ?: 0
        }
    }

    override fun getChangeListener(): OnRangeChangedListener {
        return rangeChangedListener
    }
}