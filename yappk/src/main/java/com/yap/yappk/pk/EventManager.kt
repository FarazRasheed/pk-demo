package com.yap.yappk.pk

import com.yap.yappk.pk.configurations.AppConfigurations
import com.yap.yappk.pk.utils.enums.PkAppEvent
import javax.inject.Inject

class EventManager @Inject constructor() {
    fun invokeEvent(appEvent: PkAppEvent) {
        when (appEvent) {
            PkAppEvent.LOGOUT -> {
                AppConfigurations.getAppConfigs()?.callBack?.invoke(appEvent)
                AppConfigurations.deInit()
            }
            else -> {
                AppConfigurations.getAppConfigs()?.callBack?.invoke(appEvent)
            }
        }
    }
}