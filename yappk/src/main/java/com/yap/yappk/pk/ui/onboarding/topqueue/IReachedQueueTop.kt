package com.yap.yappk.pk.ui.onboarding.topqueue

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.networking.microservices.customers.requestsdtos.CompleteVerificationRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo

interface IReachedQueueTop {
    interface View : IBase.View<ViewModel> {
        fun setCardAnimation()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val isCompleteVerification: LiveData<Boolean>
        val openCompleteVerification: LiveData<SingleEvent<AccountInfo?>>
        fun completeVerification(completeVerificationRequest: CompleteVerificationRequest)
        fun getCompleteVerificationRequest(): CompleteVerificationRequest
        fun openCompleteVerificationScreen()
    }

    interface State : IBase.State {
        var firstName: ObservableField<String>
        var countryCode: ObservableField<String>
        var mobileNo: ObservableField<String>
    }
}