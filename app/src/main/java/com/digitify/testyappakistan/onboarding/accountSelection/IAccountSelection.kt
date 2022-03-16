/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 3:17 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 3:17 PM
 *
 */

package com.digitify.testyappakistan.onboarding.accountSelection

import com.digitify.core.base.interfaces.IBase

interface IAccountSelection {

    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>

    interface State : IBase.State
}