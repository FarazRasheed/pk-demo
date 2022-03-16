package com.yap.yappk.pk.ui.kyc.secretquestions.birthcity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.VerifySecretQuestionsRequest
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.secretquestions.adapter.SecretQuestionsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlaceOfBirthVM @Inject constructor(
    override val viewState: PlaceOfBirthState,
    private val customersApi: CustomersApi,
    private val sessionManager: SessionManager
) : BaseViewModel<IPlaceOfBirth.State>(), IPlaceOfBirth.ViewModel {
    override val secretQuestionsAdapter: SecretQuestionsAdapter =
        SecretQuestionsAdapter(arrayListOf())

    private val _isVerified: MutableLiveData<Boolean> = MutableLiveData()
    override val isVerified: LiveData<Boolean> = _isVerified

    private val _cityOfBirthNamesList: MutableLiveData<MutableList<String>> = MutableLiveData()
    override val cityOfBirthNamesList: LiveData<MutableList<String>> = _cityOfBirthNamesList

    override var placeOfBirthCityAnswer: String? = null
    override var mothersMaidenNameAnswer: String? = null

    override fun onCreate() {
        super.onCreate()
        if (_cityOfBirthNamesList.value.isNullOrEmpty())
            getCityOfBirthNames()
    }

    fun getCityOfBirthNames() {
        launch {
            showLoading(true)
            val response = customersApi.getPlaceOfBirthCities()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _cityOfBirthNamesList.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _cityOfBirthNamesList.value = arrayListOf()
                    }
                }
            }
        }
    }

    override fun verifyQuestionAnswers(request: VerifySecretQuestionsRequest) {
        launch {
            showLoading(true)
            val response = customersApi.verifySecretQuestions(request)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        getAccountInfo()
                    }

                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _isVerified.value = false
                    }
                }
            }
        }
    }

    private fun getAccountInfo() {
        launch(Dispatcher.Main) {
            sessionManager.getAccountInfo { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    _isVerified.value = true
                    hideLoading()
                } else {
                    hideLoading()
                    showAlertMessage(errorMessage)
                    _isVerified.value = false
                }
            }
        }
    }
}