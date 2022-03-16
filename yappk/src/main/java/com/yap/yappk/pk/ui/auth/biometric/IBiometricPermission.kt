package com.yap.yappk.pk.ui.auth.biometric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SharedPreferenceManager
import com.digitify.core.utils.SingleEvent

interface IBiometricPermission {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var termsURL: String
        val sharedPreferenceManager: SharedPreferenceManager
        val openNotification: LiveData<SingleEvent<Int>>
        val openDashboard: LiveData<SingleEvent<Int>>
        fun openNotificationScreen()
        fun openDashboardScreen()
        fun saveUserSettings(isGranted: Boolean)
        fun navigate()
        fun setBiometricSetting(isGranted: Boolean)
        fun setNotificationSetting(isGranted: Boolean)
        fun setupViews()
    }

    interface State : IBase.State {
        val screenType: MutableLiveData<String>
        val screenTitle: MutableLiveData<String>
        val screenDescription: MutableLiveData<String>
        val buttonTitle: MutableLiveData<String>
        val icon: MutableLiveData<Int>
    }
}
