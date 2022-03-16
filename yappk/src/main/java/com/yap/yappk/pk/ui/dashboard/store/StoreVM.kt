package com.yap.yappk.pk.ui.dashboard.store

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreVM @Inject constructor(override val viewState: StoreState) :
    BaseViewModel<IStore.State>(), IStore.ViewModel