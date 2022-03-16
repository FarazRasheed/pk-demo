package com.yap.yappk.pk.ui.auth.biometric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.*
import com.yap.yappk.R
import com.yap.yappk.localization.*
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiometricPermissionVM @Inject constructor(
    override val viewState: BiometricPermissionState,
    private val resourcesProviders: ResourcesProviders,
    override val sharedPreferenceManager: SharedPreferenceManager
) : BaseViewModel<IBiometricPermission.State>(), IBiometricPermission.ViewModel {
    override var termsURL: String = ""

    private val _openNotification: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openNotification: LiveData<SingleEvent<Int>> = _openNotification

    private val _openDashboard: MutableLiveData<SingleEvent<Int>> = MutableLiveData()
    override val openDashboard: LiveData<SingleEvent<Int>> = _openDashboard

    override fun openNotificationScreen() {
        _openNotification.value = SingleEvent(R.id.action_biometricPermissionFragment_self)
    }

    override fun openDashboardScreen() {
        _openDashboard.value = SingleEvent(-1)
    }

    override fun saveUserSettings(isGranted: Boolean) {
        if (viewState.screenType.value == TOUCH_ID_SCREEN_TYPE)
            setBiometricSetting(isGranted)
        else
            setNotificationSetting(isGranted)
    }

    override fun navigate() {
        if (viewState.screenType.value == TOUCH_ID_SCREEN_TYPE)
            openNotificationScreen()
        else
            openDashboardScreen()
    }

    override fun setBiometricSetting(isGranted: Boolean) {
        when (isGranted) {
            true -> {
                sharedPreferenceManager.save(
                    KEY_TOUCH_ID_ENABLED,
                    true
                )
            }
            else -> {
                sharedPreferenceManager.save(
                    KEY_TOUCH_ID_ENABLED,
                    false
                )
            }
        }
    }

    override fun setNotificationSetting(isGranted: Boolean) {
        when (isGranted) {
            true -> {
                sharedPreferenceManager.save(
                    ENABLE_LEAN_PLUM_NOTIFICATIONS,
                    true
                )
            }
            else -> {
                sharedPreferenceManager.save(
                    ENABLE_LEAN_PLUM_NOTIFICATIONS,
                    false
                )
            }
        }
    }

    override fun setupViews() {
        when (viewState.screenType.value) {

            TOUCH_ID_SCREEN_TYPE -> {
                viewState.screenTitle.value =
                    resourcesProviders.getString(screen_touch_id_text_title)
                viewState.buttonTitle.value =
                    resourcesProviders.getString(screen_touch_id_text_button)
                viewState.screenDescription.value =
                    resourcesProviders.getString(screen_touch_id_text_description)

                viewState.icon.value = R.drawable.ic_touch_id
            }

            NOTIFICATION_SCREEN_TYPE -> {
                viewState.screenTitle.value =
                    resourcesProviders.getString(screen_notification_setting_text_title)
                viewState.buttonTitle.value =
                    resourcesProviders.getString(
                        screen_notification_setting_text_yes_keep_up_to_date
                    )
                viewState.screenDescription.value =
                    resourcesProviders.getString(screen_notification_setting_description)

                viewState.icon.value = R.drawable.pk_ic_notification
            }
        }
    }
}
