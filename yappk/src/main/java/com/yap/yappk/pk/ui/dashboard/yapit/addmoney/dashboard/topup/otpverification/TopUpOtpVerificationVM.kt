package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.topup.otpverification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopUpOtpVerificationVM @Inject constructor(
    override val viewState: TopUpOtpVerificationState
) :
    BaseViewModel<ITopUpOtpVerification.State>(), ITopUpOtpVerification.ViewModel {

    private val _htmlUrl: MutableLiveData<String> = MutableLiveData()
    override val htmlUrl: LiveData<String> = _htmlUrl
    override fun setUrl(url: String?) {
        _htmlUrl.value = url
    }
}