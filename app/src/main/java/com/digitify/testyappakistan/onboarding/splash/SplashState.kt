/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 11:16 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 11:16 AM
 *
 */

package com.digitify.testyappakistan.onboarding.splash

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState

class SplashState : BaseState(), ISplash.State {
    override var selectedRegion: MutableLiveData<String?> = MutableLiveData()
}