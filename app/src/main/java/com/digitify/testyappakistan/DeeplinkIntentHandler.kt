package com.digitify.testyappakistan

import android.content.Intent
import com.digitify.testyappakistan.utils.Constants.FLOW_ID
import com.digitify.testyappakistan.utils.Constants.REGION
import javax.inject.Inject

class DeeplinkIntentHandler @Inject constructor() {
    fun getDeeplinkIntentData(intent: Intent): HashMap<String, String> {
        if (intent.data == null || intent.data?.isHierarchical == false) return hashMapOf()
        val flowId: String? = intent.data?.getQueryParameter(FLOW_ID) ?: run {
            intent.getBundleExtra(FLOW_ID)?.getString(FLOW_ID)
        } ?: run {
            intent.getStringExtra(FLOW_ID)
        }

        val region: String? = intent.data?.getQueryParameter(REGION) ?: run {
            intent.getBundleExtra(REGION)?.getString(REGION)
        } ?: run {
            intent.getStringExtra(REGION)
        }

        return generateHashmap(flowId, region)

    }

    private fun generateHashmap(
        flowId: String?,
        regionId: String?,
    ): HashMap<String, String> {
        val hashtable: HashMap<String, String> = hashMapOf()

        flowId?.let { id ->
            hashtable[FLOW_ID] = id
        }

        regionId?.let { region ->

            hashtable[REGION] = region
        }
        return hashtable
    }

    fun isSameRegion(regionId: String?, currentRegionId: String?): Boolean {
        return regionId == currentRegionId
    }
}