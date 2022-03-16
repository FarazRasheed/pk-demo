/*
 * *
 *  * Created by faheemriaz on 25/08/2021, 11:13 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 11:13 AM
 *
 */

package com.digitify.testyappakistan.onboarding.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.utils.SingleEvent

interface ISplash {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val openAuthentication: LiveData<SingleEvent<Boolean?>>
        val openDemo: LiveData<SingleEvent<Boolean?>>
        val openDemoWithLogin: LiveData<SingleEvent<Boolean?>>
        fun moveNext()
        fun openAuthenticationScreen()
        fun openDemoScreen()
        fun openDemoWithLoginScreen()
    }

    interface State : IBase.State{
        var selectedRegion: MutableLiveData<String?>
    }
}