package com.yap.yappk.pk.ui.kyc.secretquestions.mothersmaiden

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.ui.kyc.secretquestions.adapter.SecretQuestionsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MotherMaidenNameVM @Inject constructor(
    override val viewState: MothersMaidenNameState,
    private val customersApi: CustomersApi
) : BaseViewModel<IMotherMaidenName.State>(), IMotherMaidenName.ViewModel {
    override val secretQuestionsAdapter: SecretQuestionsAdapter = SecretQuestionsAdapter(
        arrayListOf()
    )

    private val _motherNamesList: MutableLiveData<MutableList<String>> = MutableLiveData()
    override val motherNamesList: LiveData<MutableList<String>> = _motherNamesList

    override var mothersName: String? = null

    override fun onCreate() {
        super.onCreate()
        if (_motherNamesList.value.isNullOrEmpty())
            getMotherMaidenNames()
    }

    fun getMotherMaidenNames() {
        launch {
            showLoading(true)
            val response = customersApi.getMotherMaidenNames()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _motherNamesList.value = response.data.data
                        hideLoading()
                    }
                    is ApiResponse.Error -> {
                        hideLoading()
                        showAlertMessage(response.error.message)
                        _motherNamesList.value = arrayListOf()
                    }
                }
            }
        }
    }
}