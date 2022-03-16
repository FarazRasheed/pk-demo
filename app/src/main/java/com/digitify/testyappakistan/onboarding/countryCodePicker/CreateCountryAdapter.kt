/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 4:52 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 4:52 PM
 *
 */

package com.digitify.testyappakistan.onboarding.countryCodePicker

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.featureflag.KEY_FLAG_PK
import com.digitify.testyappakistan.featureflag.KEY_FLAG_UAE
import com.digitify.testyappakistan.featureflag.SPFeatureFlagClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateCountryAdapter @Inject constructor(
    private var featureFlags: SPFeatureFlagClient
) {

    fun getAdapter(context: Context): CountryCodeAdapter {
        val adapter = CountryCodeAdapter(mutableListOf())
        val itemList: ArrayList<CountryItem> = arrayListOf()
        itemList.add(
            CountryItem(
                0,
                "Select Country",
                "",
                AppCompatResources.getDrawable(context, R.drawable.flag_pk),
                AppCompatResources.getDrawable(context, R.drawable.flag_pk),
            )
        )

        featureFlags.hasFeature(KEY_FLAG_PK) { hasFeature ->
            if (hasFeature) {
                itemList.add(
                    CountryItem(
                        1,
                        "Pakistan",
                        "+92",
                        AppCompatResources.getDrawable(context, R.drawable.flag_pk),
                        AppCompatResources.getDrawable(context, R.drawable.draw_icon_pk)
                    )
                )
            }
        }

        featureFlags.hasFeature(KEY_FLAG_UAE) { hasFeature ->
            if (hasFeature) {
                itemList.add(
                    CountryItem(
                        2,
                        "United Arab Emirates",
                        "+971",
                        AppCompatResources.getDrawable(context, R.drawable.ic_uae_flag),
                        AppCompatResources.getDrawable(context, R.drawable.draw_icon)
                    )
                )
            }
        }
//        adapter.setList(itemList)

        return CountryCodeAdapter(itemList)
    }
}