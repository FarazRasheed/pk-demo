package com.yap.yappk.pk.ui.kyc.selfie.selfiereview

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class SelfieReviewState @Inject constructor() : BaseState(), ISelfieReview.State {
    override val filePath: MutableLiveData<String> = MutableLiveData("")
}