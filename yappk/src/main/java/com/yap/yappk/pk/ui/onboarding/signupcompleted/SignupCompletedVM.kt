package com.yap.yappk.pk.ui.onboarding.signupcompleted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.utils.maskIbanNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupCompletedVM @Inject constructor(
    override val viewState: SignupCompletedState,
    var resourcesProviders: ResourcesProviders,
    override val sessionManager: SessionManager
) : BaseViewModel<ISignupCompleted.State>(), ISignupCompleted.ViewModel {
    override var elapsedOnboardingTime: Long = 0
    override var startTime: Long = 0

    private val _openKycDashboard: MutableLiveData<SingleEvent<Any>> = MutableLiveData()
    override val openKycDashboard: LiveData<SingleEvent<Any>> = _openKycDashboard

    private val _openWaitingList: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openWaitingList: LiveData<SingleEvent<Int>> = _openWaitingList

    override fun onCreate() {
        super.onCreate()
        if (!sessionManager.userAccount.value?.iban.isNullOrBlank())
            viewState.ibanNumber.value = sessionManager.userAccount.value?.iban.maskIbanNumber()
    }

    override fun navigate() {
        if (viewState.isWaiting.value == true)
            openWaitingList()
        else
            openDashboard()
    }

    override fun openDashboard() {
        _openKycDashboard.value = SingleEvent(-1)
    }

    override fun openWaitingList() {
        _openWaitingList.value =
            SingleEvent(R.id.action_signupCompletedFragment_to_waitingListFragment)
    }
}