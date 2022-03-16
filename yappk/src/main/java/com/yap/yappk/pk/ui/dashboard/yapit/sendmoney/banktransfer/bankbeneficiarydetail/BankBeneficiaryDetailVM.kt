package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.localization.common_button_cancel
import com.yap.yappk.localization.screen_bank_transfer_beneficiary_detail_display_choose
import com.yap.yappk.localization.screen_bank_transfer_beneficiary_detail_display_take
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BankBeneficiaryDetailVM @Inject constructor(
    override val viewState: BankBeneficiaryDetailState,
    private val customersApi: CustomersApi,
    private val resourcesProvider: ResourcesProviders
) : BaseViewModel<IBankBeneficiaryDetail.State>(), IBankBeneficiaryDetail.ViewModel {
    private val _beneficiary: MutableLiveData<IBeneficiary> = MutableLiveData()
    override val beneficiary: LiveData<IBeneficiary> = _beneficiary

    private val _isBeneficiaryDeleted = MutableLiveData<Boolean>()
    override val isBeneficiaryDeleted: LiveData<Boolean> get() = _isBeneficiaryDeleted

    private val _isBeneficiaryUpdated = MutableLiveData<Boolean>()
    override val isBeneficiaryUpdated: LiveData<Boolean> get() = _isBeneficiaryUpdated

    override fun setBeneficiary(beneficiary: IBeneficiary?) {
        _beneficiary.value = beneficiary
    }

    fun onCardNameTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        viewState.valid.value = s.toString().isNotEmpty()
    }

    override fun getChooseOptionList(): ArrayList<String> {
        val optionList: ArrayList<String> = arrayListOf()
        optionList.add(
            resourcesProvider.getString(
                screen_bank_transfer_beneficiary_detail_display_choose
            )
        )
        optionList.add(
            resourcesProvider.getString(
                screen_bank_transfer_beneficiary_detail_display_take
            )
        )
        optionList.add(resourcesProvider.getString(common_button_cancel))
        return optionList
    }

    override fun removeBeneficiary(beneficiaryId: String) {
        launch {
            showLoading(true)
            val response = customersApi.deleteIBFTBeneficiary(beneficiaryId)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _isBeneficiaryDeleted.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isBeneficiaryDeleted.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }

    override fun updateBeneficiary(beneficiaryId: String, nickName: String) {
        launch {
            showLoading(true)
            val response =
                customersApi.updateBankBeneficiary(
                    profilePicture = null,
                    id = beneficiaryId,
                    nickName = nickName
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _isBeneficiaryUpdated.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _isBeneficiaryUpdated.value = false
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }
}