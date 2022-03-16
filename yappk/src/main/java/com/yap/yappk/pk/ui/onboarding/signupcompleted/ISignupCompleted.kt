package com.yap.yappk.pk.ui.onboarding.signupcompleted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.pk.SessionManager

interface ISignupCompleted {
    interface View : IBase.View<ViewModel> {
        val completeProgressValue get() = 100
        val startAnimationDelay: Long get() = 500
        val parallelAnimationDelay: Long get() = 200
        val ibanAnimationDelay: Long get() = 100
        val noteAnimationDelay: Long get() = 200
        val buttonAnimationDelay: Long get() = 300
        val titleAnimationDelay: Long get() = 100
        val descriptionAnimationDelay: Long get() = 50
        val completeTitleAnimationDelay: Long get() = 300
        val counterAnimationDelay: Long get() = 1500
        val slideAnimationDelay: Long get() = 500
    }

    interface ViewModel : IBase.ViewModel<State> {
        var elapsedOnboardingTime: Long
        var startTime: Long
        val sessionManager: SessionManager
        val openKycDashboard: LiveData<SingleEvent<Any>>
        val openWaitingList: LiveData<SingleEvent<Int>>
        fun openDashboard()
        fun openWaitingList()
        fun navigate()
    }

    interface State : IBase.State {
        var name: MutableLiveData<String>
        var description: MutableLiveData<CharSequence>
        val isWaiting: MutableLiveData<Boolean>
        val ibanNumber: MutableLiveData<String>
    }
}