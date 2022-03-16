package com.yap.yappk.pk.deeplinkNavigations

import com.digitify.core.deeplink.DeeplinkNavigatorPayload
import com.digitify.core.deeplink.IDeeplinkNavigator
import javax.inject.Inject

class NotificationPayloadImpl @Inject constructor(
    private val deeplinkHandler: IDeeplinkNavigator
) : DeeplinkNavigatorPayload {

    override fun onReceivePayload(payload: HashMap<String, String>?) {
        deeplinkHandler.handlePayload(payload)
    }
}