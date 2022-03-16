package com.yap.yappk.pk.configurations.onboarding

import androidx.lifecycle.ViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    lateinit var customersApi: CustomersApi

    fun verifyUser(
        user: String,
        callback: (ApiResponse<BaseApiResponse>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = customersApi.verifyUser(user)
            withContext(Dispatchers.Main) { callback(response) }
        }
    }

}