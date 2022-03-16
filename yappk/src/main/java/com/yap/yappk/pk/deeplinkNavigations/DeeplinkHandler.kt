package com.yap.yappk.pk.deeplinkNavigations

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.digitify.core.deeplink.IDeeplinkNavigator
import com.yap.yappk.pk.deeplinkNavigations.sealed.DeeplinkFlowId
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeeplinkHandler @Inject constructor(
    @ApplicationContext val context: Context
) : IDeeplinkNavigator {
    private val _deeplinkFlowId: MutableLiveData<String> = MutableLiveData()
    private fun handleDeeplinkFlow(flowId: String?) {

        flowId.let {
            Log.e("DeeplinkNavigatorvcvbcvbcbcv===>", flowId.toString())
            when (it) {
                DeeplinkFlowId.TransactionDetails.flowId -> {
                    // Navigate to specific View
                    Log.e("DeeplinkNavigatorvcvbcvbcbcv===>", flowId.toString())

                }
                else -> {

                }
            }
        }
    }

    override fun handlePayload(payload: HashMap<String, String>?) {
        payload?.let {
            _deeplinkFlowId.value = getFlowIdFromPayload(payload)
        }
    }

    override fun onDataLoaded(context: Context) {
        _deeplinkFlowId.value?.let {flowId ->
            handleDeeplinkFlow(flowId)
        }
    }

    private fun getFlowIdFromPayload(payload: HashMap<String, String>): String? {
        return payload["flow_id"]
    }
}