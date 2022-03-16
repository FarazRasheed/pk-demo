/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 3:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 3:23 PM
 *
 */

package com.digitify.testyappakistan.onboarding.accountSelection

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountSelectionVM @Inject constructor() : BaseViewModel<IAccountSelection.State>(),
    IAccountSelection.ViewModel {
    override val viewState: IAccountSelection.State = AccountSelectionState()

}