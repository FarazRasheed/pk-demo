package com.yap.yappk.pk.configurations.onboarding

import androidx.lifecycle.ViewModel
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.microservices.messages.MessagesApi
import com.yap.yappk.networking.microservices.messages.requestdtos.CreateOtpOnboardingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor() : ViewModel() {

    lateinit var messagesApi: MessagesApi

    fun createOtp(
        request: CreateOtpOnboardingRequest,
        callback: (ApiResponse<BaseApiResponse>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = messagesApi.createOtpOnboarding(request)
            withContext(Dispatchers.Main) { callback(response) }
        }
    }

}