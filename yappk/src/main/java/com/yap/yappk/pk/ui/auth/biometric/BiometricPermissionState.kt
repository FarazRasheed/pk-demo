package com.yap.yappk.pk.ui.auth.biometric

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class BiometricPermissionState @Inject constructor() : BaseState(), IBiometricPermission.State {
    override val screenType: MutableLiveData<String> = MutableLiveData("")
    override val screenTitle: MutableLiveData<String> = MutableLiveData("")
    override val screenDescription: MutableLiveData<String> = MutableLiveData("")
    override val buttonTitle: MutableLiveData<String> = MutableLiveData("")
    override val icon: MutableLiveData<Int> = MutableLiveData(0)
}
