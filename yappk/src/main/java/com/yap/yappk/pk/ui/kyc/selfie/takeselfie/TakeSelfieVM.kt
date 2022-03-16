package com.yap.yappk.pk.ui.kyc.selfie.takeselfie

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TakeSelfieVM @Inject constructor(
    override val viewState: TakeSelfieState
) : BaseViewModel<ITakeSelfie.State>(), ITakeSelfie.ViewModel {

}