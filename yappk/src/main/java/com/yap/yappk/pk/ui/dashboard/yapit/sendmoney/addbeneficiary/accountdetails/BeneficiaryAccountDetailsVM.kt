package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CreateOtpUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStateModel
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.adapter.AddBeneficiaryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BeneficiaryAccountDetailsVM @Inject constructor(
    override val viewState: BeneficiaryAccountDetailsState,
    private val createOtpUC: CreateOtpUC,
    private val customersApi: CustomersApi
) :
    BaseViewModel<IBeneficiaryAccountDetails.State>(), IBeneficiaryAccountDetails.ViewModel {

    private var _bankAccountDetails: MutableLiveData<BankData> = MutableLiveData()
    override val bankAccountDetails: LiveData<BankData> = _bankAccountDetails
    private var _createOtpSuccess: MutableLiveData<Boolean> = MutableLiveData()
    override val createOtpSuccess: LiveData<Boolean> = _createOtpSuccess
    private var _addBeneficiarySuccess: MutableLiveData<Boolean> = MutableLiveData()
    override val addBeneficiarySuccess: LiveData<Boolean> = _addBeneficiarySuccess
    private val _errorDescription = MutableLiveData<CharSequence>()
    override val errorDescription: LiveData<CharSequence> = _errorDescription

    override fun setBankAccountDetails(bankAccountInfo: BankData?) {
        _bankAccountDetails.value = bankAccountInfo
    }

    override fun onTextChange(number: CharSequence, start: Int, before: Int, count: Int) {
        viewState.isValidaNickName.value = when {
            number.length > 1 -> true
            else -> false
        }
    }

    override fun createOtp(action: String) {
        showLoading()
        createOtpUC.executeUseCase(
            CreateOtpUC.RequestValues(action),
            object :
                UseCaseCallback<CreateOtpUC.ResponseValue, CreateOtpUC.ErrorValue> {
                override fun onSuccess(response: CreateOtpUC.ResponseValue) {
                    hideLoading()
                    _createOtpSuccess.value = true
                }

                override fun onError(error: CreateOtpUC.ErrorValue) {
                    hideLoading()
                    _createOtpSuccess.value = false
                    _errorDescription.value = error.msg
                }
            })
    }

    override fun addBeneficiaryRequest(
        title: String?,
        accountNo: String?,
        bankName: String?,
        nickName: String?,
        beneficiaryType: String?
    ) {
        launch {
            showLoading(true)
            val response =
                customersApi.addIBFTBeneficiary(
                    title = title,
                    accountNo = accountNo,
                    bankName = bankName,
                    nickName = nickName,
                    beneficiaryType = beneficiaryType
                )
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        hideLoading()
                        _addBeneficiarySuccess.value = true
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        _errorDescription.value = response.error.message
                    }
                }
            }
        }
    }

    override fun handleState(): ArrayList<AddBeneficiaryStateModel> {
        val list = ArrayList<AddBeneficiaryStateModel>()
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.IN_PROGRESS.name
            )
        )
        return list
    }

    override fun handleCompleteState(): ArrayList<AddBeneficiaryStateModel> {
        val list = ArrayList<AddBeneficiaryStateModel>()
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
        list.add(
            AddBeneficiaryStateModel(
                markerState = AddBeneficiaryStates.DONE.name
            )
        )
        return list
    }
}