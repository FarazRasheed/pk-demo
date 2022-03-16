package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail.updateprofileimage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateBankBeneficiaryImageVM @Inject constructor(
    override val viewState: UpdateBankBeneficiaryImageState,
    private val customersApi: CustomersApi,
    private val resourcesProvider: ResourcesProviders
) : BaseViewModel<IUpdateBankBeneficiaryImage.State>(), IUpdateBankBeneficiaryImage.ViewModel {

    private val _beneficiary: MutableLiveData<IBeneficiary> = MutableLiveData()
    override val beneficiary: LiveData<IBeneficiary> = _beneficiary

    private val _beneficiaryUpdated= MutableLiveData<IBeneficiary?>()
    override val beneficiaryUpdated: LiveData<IBeneficiary?> get() = _beneficiaryUpdated

    override fun setBeneficiary(beneficiary: IBeneficiary?) {
        _beneficiary.value = beneficiary
    }

    override fun updateBeneficiary(beneficiaryId: String, file: File?) {
        launch {
            showLoading(true)
            val response =
                customersApi.updateBankBeneficiary(
                    profilePicture = file,
                    id = beneficiaryId,
                    nickName = null
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _beneficiaryUpdated.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _beneficiaryUpdated.value = null
                        showAlertMessage(response.error.message)
                    }
                }
            }
        }
    }
}
