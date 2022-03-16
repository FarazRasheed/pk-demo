package com.yap.yappk.pk.ui.kyc.selfie.selfieguide

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelfieGuideVM @Inject constructor(
    override val viewState: SelfieGuideState
) : BaseViewModel<ISelfieGuide.State>(), ISelfieGuide.ViewModel {

}