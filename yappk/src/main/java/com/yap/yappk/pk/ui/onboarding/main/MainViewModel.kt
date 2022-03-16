package com.yap.yappk.pk.ui.onboarding.main

import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.SingleClickEvent
import com.yap.yappk.networking.microservices.customers.requestsdtos.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<IMain.State>(), IMain.ViewModel {
    override val viewState: IMain.State = MainState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val signupData: SignUpRequest = SignUpRequest()

    override fun setProgress(percent: Int) {
        viewState.currentProgress.set(percent)
    }

    override fun setProgressVisibility(visible: Boolean) {
        viewState.toolsBarVisibility.set(visible)
    }


}